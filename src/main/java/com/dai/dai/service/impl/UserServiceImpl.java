package com.dai.dai.service.impl;

import com.dai.dai.converter.user.UserConverter;
import com.dai.dai.dto.user.UserDto;
import com.dai.dai.entity.UserEntity;
import com.dai.dai.exception.handler.ConflictException;
import com.dai.dai.repository.UserRepository;
import com.dai.dai.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public UserDto getUserInfoById(Integer userId) {
        log.info("[UserService] Comienza la ejecución del metodo getUserInfoById(). UserId: {}.",userId);
        Optional<UserEntity> UserResponse = null;

        try{
            UserResponse = userRepository.findById(userId);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new RuntimeException("Ocurrio un error al consultar la base de datos.");
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
                StringUtils.isEmpty(userDto.getProfile_image())) {
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
        user.setProfile_image(userDto.getProfile_image());
        var userSave = userRepository.save(user);
        userDto.setId(userSave.getId());
        return userDto;
    }
}
