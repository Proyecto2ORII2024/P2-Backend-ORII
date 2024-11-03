package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("UPDATE UserEntity u SET u.password = :newPassword WHERE u.id = :id")
    boolean updatePassword(Long id, String newPassword);

}
