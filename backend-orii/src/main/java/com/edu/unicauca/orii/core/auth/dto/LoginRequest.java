package com.edu.unicauca.orii.core.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$", message = "The email domain is not allowed")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;
}
