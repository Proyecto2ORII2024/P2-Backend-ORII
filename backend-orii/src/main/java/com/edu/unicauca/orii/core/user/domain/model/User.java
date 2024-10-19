package com.edu.unicauca.orii.core.user.domain.model;

import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class User {
    private Long userId;
    private String email;
    private String password;
    private Date updatePassword;
    private RoleEnum role;
}
