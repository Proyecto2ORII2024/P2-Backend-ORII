package com.edu.unicauca.orii.core.user.domain.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.mobility.domain.model.Form;
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
    private FacultyEnum faculty;

    private RoleEnum role;
    /** A list of forms associated with the user. */
    private List<Form> forms;

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
