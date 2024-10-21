package com.edu.unicauca.orii.core.user.domain.model;

import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long userId;
    private String email;
    private String password;
    private Date updatePassword;
    private Boolean EmailVerified;
    private RoleEnum role;

}
