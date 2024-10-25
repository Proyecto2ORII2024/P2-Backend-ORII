package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response;

import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
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

    private String email;

    private Boolean emailVerified;

    private RoleEnum role;

    private Date updatePassword;
}
