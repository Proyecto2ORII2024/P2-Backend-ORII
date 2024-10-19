package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserCreateRequest {
     @JsonIgnore
    private Long userId;

    @NotNull(message = "The email is required")
    private String email;

    @NotNull(message = "The password is required")
    private String password;

    @NotBlank(message ="The role is required")
    private RoleEnum role;

}
