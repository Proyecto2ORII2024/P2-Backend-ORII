package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;

public interface IFormRepository extends JpaRepository<FormEntity, Long> {

    List<FormEntity> findByAgreement_AgreementId(Long id);

    @Modifying
    @Transactional
    @Query("update FormEntity f set f.user = null where f.user.id = ?1")
    void updateFormUser(Long userId);
    
}
