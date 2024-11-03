package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdatePasswordRequest {

    @NotBlank(message = "The id user is required")
    private long id;

    @NotBlank(message = "The actual password is required")
    private String actualPassword;

    @NotBlank(message = "The new password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!&])[A-Za-z\\d@$!&]{8,16}$",
        message = "The new password must be 8-16 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character (@, $, !, or &)"
    )
    private String newPassword;
}
