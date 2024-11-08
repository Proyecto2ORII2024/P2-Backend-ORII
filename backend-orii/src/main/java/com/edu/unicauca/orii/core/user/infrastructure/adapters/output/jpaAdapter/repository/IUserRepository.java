package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;


public interface IUserRepository extends JpaRepository<UserEntity, Long> {
 UserEntity findByEmail(String email);
 boolean existsByEmailAndEmailVerifiedTrue(String email);

}
