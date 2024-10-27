package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.EmailTokenEntity;

public interface IEmailTokenRepository extends JpaRepository<EmailTokenEntity, Long> {
    Optional<EmailTokenEntity> findByToken(String token);

}
