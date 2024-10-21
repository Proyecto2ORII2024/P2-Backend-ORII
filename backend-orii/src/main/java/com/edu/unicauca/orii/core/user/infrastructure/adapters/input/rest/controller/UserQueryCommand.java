package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.user.application.service.UserQueryService;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;



import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")

public class UserQueryCommand {
    private final UserQueryService userQueryService;
    private final IUserRestMapper userRestMapper;

    @GetMapping("/get")
    public void getUser() {

    }

    @GetMapping("/get/{id}")
    public void getUserById() {

    }
}
