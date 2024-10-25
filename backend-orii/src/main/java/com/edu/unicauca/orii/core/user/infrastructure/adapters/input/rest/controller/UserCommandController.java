package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

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
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserUpdateRequest;
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
        user = userCommandService.createUser(user);
        return ResponseEntity.created(null).body(userRestMapper.toUserData(user));
    }

    /**
     * Updates the email and role of a user if their email is not verified.
     * 
     * @param id                the ID of the {@link User} to be updated
     * @param userUpdateRequest the new email and role values encapsulated in a
     *                          {@link UserUpdateRequest} object
     * @return a {@link ResponseEntity} containing the updated {@link UserData} object and a status of 200 OK      
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userRestMapper.toUser(userUpdateRequest);
        User updatedUser = userCommandService.updateUser(id, user);

        return ResponseEntity.ok(userRestMapper.toUserData(updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
        userCommandService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

}
