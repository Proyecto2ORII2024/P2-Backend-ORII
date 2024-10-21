package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.user.application.service.UserCommandService;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCreateRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;


import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    public void createUser() {

    }

    @PutMapping("/update/{id}")
    public void updateUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        
    }
}
