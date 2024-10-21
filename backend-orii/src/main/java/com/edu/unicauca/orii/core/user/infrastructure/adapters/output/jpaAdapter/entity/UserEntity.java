package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity;

import java.util.Date;

import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id_user")
    private Long userId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Boolean EmailVerified;

    @Column(name="user_role", nullable = false, length = 24)
    private RoleEnum role;

    @Column(nullable = false)   
    private Date updatePassword;

    @OneToOne(mappedBy = "user")
    EmailTokenEntity emailToken;

}
