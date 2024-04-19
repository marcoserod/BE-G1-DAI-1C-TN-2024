package com.dai.dai.service.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.converter.user.UserConverter;
import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.entity.UserEntity;
import com.dai.dai.entity.UserFavoriteEntity;
import com.dai.dai.exception.TmdbNotFoundException;
import com.dai.dai.exception.handler.ConflictException;
import com.dai.dai.repository.UserFavoriteRepository;
import com.dai.dai.repository.UserRepository;
import com.dai.dai.service.MovieService;
import com.dai.dai.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserFavoriteRepository userFavoriteRepository;
    MovieService movieService;

    @Override
    public UserDto getUserInfoById(Integer userId) {
        log.info("[UserService] Comienza la ejecución del metodo getUserInfoById(). UserId: {}.",userId);
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
    public UserDto createUser(UserDto userDto) {

        if (StringUtils.isEmpty(userDto.getName()) ||
                StringUtils.isEmpty(userDto.getEmail()) ||
                StringUtils.isEmpty(userDto.getSurname()) ||
                StringUtils.isEmpty(userDto.getNickname())  ||
                StringUtils.isEmpty(userDto.getProfileImage())) {
            throw new IllegalArgumentException("All parameters are required");
        }

        if (userRepository.findByNickname(userDto.getNickname()) != null) {
            throw new ConflictException("Existing nickname");
        }

        //First approach without google sign in
        var user = new UserEntity();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setSurname(userDto.getSurname());
        user.setNickname(userDto.getNickname());
        user.setProfile_image(userDto.getProfileImage());
        List<UserFavoriteEntity> favorites = new ArrayList<>();
        user.setFavorites(favorites);

        var userSave = userRepository.save(user);
        userDto.setId(userSave.getId());
        return userDto;
    }

    @Override
    public void addFavorite(Integer userId, Integer filmId) {

        Optional<UserEntity> userOptional;

        try{
            userOptional = userRepository.findById(Integer.valueOf(userId));
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }

        if (userOptional.isEmpty()) {
            throw new ConflictException("User not found for userId "+ userId);
        }

        try {
            movieService.getMovieById(filmId);

            var user = userOptional.get();
            var userFavorite = new UserFavoriteEntity();
            userFavorite.setFilm_id(String.valueOf(filmId));
            userFavorite.setUser(user);
            user.getFavorites().add(userFavorite);

            userRepository.save(user);
        } catch (Exception e) {
            throw new TmdbNotFoundException("Couldn't find the movie with ID " + filmId);
        }
    }

    @Override
    public GetMoviesResponse getFavorites(Integer userID) {
        List<Movie> movies = new ArrayList<>();
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
            return GetMoviesResponse.builder()
                    .movies(movies)
                    .build();
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("An error occurred while querying the database");
        }
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
        //TODO
    }
}
