package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
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

public class UserCommonRequest {
    @JsonIgnore
    private Long userId;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$", message = "The email domain is not allowed")
    private String email;

    @NotNull(message ="The role is required")
    private RoleEnum role;

    private FacultyEnum faculty;

}
