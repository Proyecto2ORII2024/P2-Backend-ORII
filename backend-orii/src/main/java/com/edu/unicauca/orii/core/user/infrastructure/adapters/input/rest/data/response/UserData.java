package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response;

import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class UserData {
    private Long userId;

    @NotBlank(message = "The email is required")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;

    @NotNull(message = "The verify email is required")
    private Boolean EmailVerified;

    @NotNull(message ="The role is required")
    private RoleEnum role;

    @NotNull(message = "The update password is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatePassword;
}
