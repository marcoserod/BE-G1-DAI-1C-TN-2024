package com.dai.dai.controller.impl;

import com.dai.dai.controller.UserController;
import com.dai.dai.dto.user.UserDto;
import com.dai.dai.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@Tag(name = "User Controller", description = "Endpoints para operaciones relacionadas con los usuerios.")
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    UserService userService;

    @GetMapping("/details/{user_id}")
    @Override
    public ResponseEntity<UserDto> getUserInfoById(@Valid @PathVariable(value = "user_id" ) Integer userId) {
        return new ResponseEntity<>(userService.getUserInfoById(userId), HttpStatus.OK);
    }
}
