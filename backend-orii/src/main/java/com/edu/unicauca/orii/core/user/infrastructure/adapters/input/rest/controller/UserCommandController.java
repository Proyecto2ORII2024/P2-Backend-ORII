package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.common.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
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
    private final IUserQueryPersistencePort userQueryPersistencePort;

    @PostMapping("/create")
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        User user = userRestMapper.toUser(userCreateRequest);
        user.setEmailVerified(false);
        LocalDateTime localDateTime = LocalDateTime.now(); // Fecha actual
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        user.setUpdatePassword(date);
        user = userCommandService.createUser(user);
        return ResponseEntity.created(null).body(userRestMapper.toUserData(user));
    }

    /**
     * Updates the email and role of a user if their email is not verified.
     * 
     * @param id                the ID of the {@link User} to be updated
     * @param userUpdateRequest the new email and role values encapsulated in a
     *                          {@link UserUpdateRequest} object
     * @return a {@link ResponseEntity} containing the updated {@link UserData}
     *         object and a status of 200 OK
     * 
     * @throws BusinessRuleException if the user's email is verified, preventing the
     *                               update
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User existingUser = userQueryPersistencePort.getUserById(id);

        if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                    "User email is verified, cannot update.");
        }

        existingUser.setEmail(userUpdateRequest.getEmail());
        existingUser.setRole(userUpdateRequest.getRole());

        User updatedUser = userCommandService.updateUser(id, existingUser);

        return ResponseEntity.ok(userRestMapper.toUserData(updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
        userCommandService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
