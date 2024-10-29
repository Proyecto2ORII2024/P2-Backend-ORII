package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ForgotPasswordRequest {
    
    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$", message = "The email domain is not allowed")
    private String email;

}
