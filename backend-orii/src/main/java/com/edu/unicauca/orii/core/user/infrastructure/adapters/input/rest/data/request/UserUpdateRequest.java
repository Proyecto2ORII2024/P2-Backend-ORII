package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * This class represents the request object for updating a User.
 * It includes fields for email and role, with validation annotations.
 * @author SergioAriza
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
     /**
     * The user's email.
     * This field is required and must be a valid email address.
     * The email must belong to the domain "@unicauca.edu.co".
     * 
     * @NotBlank - Ensures the email field is not blank.
     * @Email - Validates that the input is in the correct email format.
     * @Pattern - Validates that the email belongs to the specified domain.
     */
    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$", message = "The email must be from the domain @unicauca.edu.co")
    private String email;

    /**
     * The user's role.
     * This field is required and must be provided as either ADMIN or USER.
     * 
     * @NotNull - Ensures the role field is not null.
     */
    @NotNull(message = "The role is required")
    private RoleEnum role;
}
