package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.ForgotPasswordRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UpdatePasswordRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCommonRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling user commands such as creating, updating, 
 * and deleting users.
 * 
 * This controller is responsible for handling incoming HTTP requests and 
 * delegating the processing of user-related commands to the appropriate services.
 * 
 * It also provides CORS configuration to allow requests from specific origins.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final IUserRestMapper userRestMapper;

     /**
     * Creates a new user based on the information provided in the 
     * {@link UserCreateRequest}.
     * 
     * @param userCreateRequest a request object containing user information such 
     *                          as email, password, and role
     * @return a {@link ResponseEntity} containing the created {@link UserData} 
     *         object and a 201 Created status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserCommonRequest userCreateRequest) {
        User user = userRestMapper.toUser(userCreateRequest);
        user = userCommandService.createUser(user);
        return ResponseEntity.created(null).body(userRestMapper.toUserData(user));
    }

    /**
     * Updates the email and role of a user if their email is not verified.
     * 
     * @param id                the ID of the {@link User} to be updated
     * @param userCommonRequest the new email and role values encapsulated in a
     *                          {@link userCommonRequest} object
     * @return a {@link ResponseEntity} containing the updated {@link UserData} object and a status of 200 OK      
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserData> updateUser(@PathVariable Long id,@Valid @RequestBody UserCommonRequest userCommonRequest) {
        User user = userRestMapper.toUser(userCommonRequest);
        User updatedUser = userCommandService.updateUser(id, user);

        return ResponseEntity.ok(userRestMapper.toUserData(updatedUser));
    }

      /**
     * Deletes a user by their ID.
     * 
     * @param id the ID of the {@link User} to be deleted
     * @return a {@link ResponseEntity} with a status of 200 OK if deletion was 
     *         successful
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
        userCommandService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
       /**
     * 
     *   recover password by email
     * 
     * @param ForgotPasswordRequest the email of the {@link User} who will recover your password
     * @return a {@link ResponseEntity} with a status of 200 OK If the password was recovered correctly
     * 
     */

    @PostMapping("/forgotpassword")
    public ResponseEntity<Boolean>  forgotPassword(@Valid @RequestBody  ForgotPasswordRequest forgotPasswordRequest){
        boolean result=this.userCommandService.forgotPassword(forgotPasswordRequest.getEmail());
        return  ResponseEntity.ok(result);
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<Boolean> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        boolean result = this.userCommandService.updatePassword(updatePasswordRequest.getUserId(), updatePasswordRequest.getActualPassword(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok(result);
    }
}
