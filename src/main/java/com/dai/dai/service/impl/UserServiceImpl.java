package com.dai.dai.service.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.converter.user.UserConverter;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.entity.UserEntity;
import com.dai.dai.entity.UserFavoriteEntity;
import com.dai.dai.exception.TmdbNotFoundException;
import com.dai.dai.exception.handler.ConflictException;
import com.dai.dai.repository.SessionRepository;
import com.dai.dai.repository.UserFavoriteRepository;
import com.dai.dai.repository.UserRepository;
import com.dai.dai.service.MovieService;
import com.dai.dai.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${movieplay.page.size}")
    Integer pageSize;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final MovieService movieService;
    private final  CloudinaryServiceImpl cloudinaryService;

    @Override
    public UserDto getUserInfoById(Integer userId) {
        log.info("[UserService] Execution of the method getUserInfoById() has started. UserId: {}.",userId);
        Optional<UserEntity> UserResponse = null;

        try{
            UserResponse = userRepository.findById(userId);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (UserResponse.isPresent()){
            log.info("User: {}", UserResponse.get());
            return UserConverter.fromUserEntityToUserDto(UserResponse.get());
        } else {
            throw new RuntimeException("User not found for userId "+userId);
        }

    }

    @Override
    public Integer createUser(UserDto userDto) throws IOException {

        var user = new UserEntity();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setSurname(userDto.getSurname());
        user.setNickname(userDto.getNickname());

        //Cloudinary
        var url = cloudinaryService.upload(userDto.getProfileImage());
        user.setProfile_image(url);
        ////

        List<UserFavoriteEntity> favorites = new ArrayList<>();
        user.setFavorites(favorites);

        var userSave = userRepository.save(user);
        return userSave.getId();
    }

    @Override
    public void addFavorite(Integer userId, Integer filmId) {

        Optional<UserEntity> userOptional;
        UserFavoriteEntity filmInFavorites;

        try{
            userOptional = userRepository.findById(Integer.valueOf(userId));
            filmInFavorites = userFavoriteRepository.findByUserIdAndFilmId(Integer.valueOf(userId), Integer.valueOf(filmId));
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (userOptional.isEmpty()) {
            throw new ConflictException("User not found for userId "+ userId);
        }

        try {
            if (filmInFavorites == null) {
                movieService.getMovieById(filmId);
                var user = userOptional.get();
                var userFavorite = new UserFavoriteEntity();
                userFavorite.setFilm_id(filmId);
                userFavorite.setUser(user);
                user.getFavorites().add(userFavorite);

                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new TmdbNotFoundException("Couldn't find the movie with ID " + filmId);
        }
    }

    @Override
    public GetFavoriteMoviesResponse getFavorites(Integer userID, Integer page) {
        List<GetMovieByIdResponse> movies = new ArrayList<>();
        Optional<UserEntity> userOptional;

        try {
            userOptional  = userRepository.findById(Integer.valueOf(userID));
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (userOptional.isEmpty()) {
            throw new ConflictException("User not found for userId "+ userID);
        }

        try {
            var listUsersFav = userFavoriteRepository.findByUserId(userID);
            for (UserFavoriteEntity favoriteFilm : listUsersFav) {
               var movie = movieService.getMovieById(Integer.valueOf(favoriteFilm.getFilm_id()));
               movies.add(movie.getMovie());
            }

            int totalPages = (int) Math.ceil((float) movies.size() / pageSize);

            var sortedResponse = paginate(page, movies, totalPages);

            return GetFavoriteMoviesResponse.builder()
                    .movies(sortedResponse)
                    .metadata(setMetadata(sortedResponse, page, totalPages, movies.size()))
                    .build();
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }
    }

    private ListMetadata setMetadata(List<GetMovieByIdResponse> movies, Integer page, Integer totalPages, Integer totalRecords){
        return ListMetadata.builder()
                .currentPage(page)
                .pageSize(movies.size())
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .build();
    }

    private List<GetMovieByIdResponse> paginate(Integer page, List<GetMovieByIdResponse> movies, Integer totalPages){
        log.info("Page {} of movies generated.", page);
        int firstItem = page * pageSize - (pageSize);
        int lastItem;
        if (totalPages < page) {
            return Collections.emptyList();
        }
        if (totalPages.equals(page) || totalPages.equals(0)){
            lastItem = movies.size();
        } else {
            lastItem = page * pageSize ;
        }
        return movies.subList(firstItem,lastItem);
    }

    @Override
    public void removeFavorite(Integer userId, Integer filmId) {
        Optional<UserEntity> userOptional;

        try {
            userOptional = userRepository.findById(Integer.valueOf(userId));
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (userOptional.isEmpty()) {
            throw new ConflictException("User not found for userId "+ userId);
        }

        var userFavorites = userOptional.get().getFavorites();
        var filmNotFound = true;

        for (UserFavoriteEntity favorite : userFavorites) {
            if (favorite.getFilm_id().equals(filmId)) {
                filmNotFound = false;
                break;
            }
        }

        if (filmNotFound) {
            throw new TmdbNotFoundException("Movie with id " + filmId + " is not in user " +
                    "favorites");
        }

        try {
            userFavoriteRepository.deleteByUserIdAndFilmId(userId, filmId);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }
    }

    @Override
    public void removeUser(Integer userID) throws IOException, InterruptedException {
        log.info("Deleting the user with ID: {}", userID);
        Optional<UserEntity> user;
        try {
          user = userRepository.findById(userID);
          if (user.isEmpty()){
              log.error("The requested user was not found.");
              throw new TmdbNotFoundException("The requested user was not found..");
          }
        } catch (Exception e){
            log.error("There was an error while searching for the user in the database.");
            throw new RuntimeException("There was an error while searching for the user in the database.");
        }
        try {
            log.info("Deleting session...");
            var session = sessionRepository.findByUserEmail(user.get().getEmail());
            if (session.isEmpty()){
                log.error("The session you want to delete was not found.");
                throw new TmdbNotFoundException("The session you want to delete was not found.");
            }
            sessionRepository.deleteById(session.get().getId());
            log.info("The user's session was successfully deleted.");
        } catch (Exception e){
            log.error("An error occurred while deleting the user's session.");
            throw new RuntimeException("An error occurred while deleting the user's session.");
        }
        try {
            userRepository.deleteById(userID);
            log.info("The user was successfully deleted.");
        } catch (Exception e){
            log.error("An error occurred while deleting the user.");
            throw new RuntimeException("An error occurred while deleting the user.");
        }
    }

    @Override
    public UserDto updateUser(String name, String surname, String nickname,
                              MultipartFile file, Integer userId) throws IOException {

        Optional<UserEntity> userOptional;

        try {
            userOptional = userRepository.findById(userId);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (userOptional.isEmpty()) {
            throw new ConflictException("User not found for userId "+ userId);
        }

        var user = userOptional.get();

        if (file != null) {
            var fileSaved = cloudinaryService.upload(file);
            var url = fileSaved.get("secure_url");
            user.setProfile_image(url.toString());
            log.info("Image updated");
        }

        if (name != null) {
            user.setName(name);
            log.info("Name updated");
        }
        if (surname != null) {
            user.setSurname(surname);
            log.info("Surname updated");
        }
        if (nickname != null) {
            var userNickname = userRepository.findByNickname(nickname);
            if (userNickname == null || userNickname.getId() == user.getId()) {
                user.setNickname(nickname);
                log.info("Nickname updated");
            } else {
                throw new ConflictException("El nombre de usuario ya está en uso.");
            }
        }

        var userUpdated = userRepository.save(user);
        return UserConverter.fromUserEntityToUserDto(userUpdated);
    }
}
