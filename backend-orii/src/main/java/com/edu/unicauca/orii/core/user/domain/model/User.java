package com.edu.unicauca.orii.core.user.domain.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long userId;
    private String email;
    private String password;
    private Date updatePassword;
    private Boolean emailVerified;
    private EmailToken emailToken;

    private RoleEnum role;

    public User() {
        initializeEmailVerified();
        UpdateDatePassword();
    }

    public void initializeEmailVerified() {
        if (null == emailVerified) {
            emailVerified = false;
        }
    }

    public void UpdateDatePassword() {
        LocalDateTime localDateTime = LocalDateTime.now(); 
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        this.updatePassword = date;
    }
}
