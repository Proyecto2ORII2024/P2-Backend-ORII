package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.user.application.service.UserCommandService;
import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCreateRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final IUserRestMapper userRestMapper;

    @PostMapping("/create")
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        User user = userRestMapper.toUser(userCreateRequest);
        user.setVerifyEmail(false);
        LocalDateTime localDateTime = LocalDateTime.now(); // Fecha actual
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        user.setUpdatePassword(date);
        user = userCommandService.createUser(user);
        return ResponseEntity.created(null).body(userRestMapper.toUserData(user));
    }

    @PutMapping("/update/{id}")
    public void updateUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
        userCommandService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
