package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.user.application.service.UserQueryService;
import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")

public class UserQueryCommand {
    private final UserQueryService userQueryService;
    private final IUserRestMapper userRestMapper;

    /**
     * Gets all the users
     * @return List of users
     * 
     */
    @GetMapping("/get")
    public List<UserData> getUsers() {
        List<User> users = userQueryService.getUser();
        return userRestMapper.toUserDataList(users);
    }

    /**
     * Gets a user by its ID
     * @param id The ID of the user to be retrieved
     * @return The user with the given ID
     */
    @GetMapping("/get/{id}")
    public UserData getUserById(@PathVariable Long id) {
        User user = userQueryService.getUserById(id);
        return userRestMapper.toUserData(user);
    }     
}
