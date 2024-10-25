package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserCreateRequest {
    @JsonIgnore
    private Long userId;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$", message = "The email must be from the domain @unicauca.edu.co")
    private String email;

    @NotNull(message ="The role is required")
    private RoleEnum role;

}
