package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = :newPassword, u.updatePassword = CURRENT_TIMESTAMP WHERE u.userId = :id")
    void updatePassword(Long id, String newPassword);

}
