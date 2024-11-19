package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityFacultyProjection;

public interface IFormRepository extends JpaRepository<FormEntity, Long> {

    List<FormEntity> findByAgreement_AgreementId(Long id);

    @Modifying
    @Transactional
    @Query("update FormEntity f set f.user = null where f.user.id = ?1")
    void updateFormUser(Long userId);
    
    @Query("SELECT faculty, output, input FROM ( " +
    "    SELECT f.faculty AS faculty, " +
    "           SUM(CASE WHEN f.direction = 0 OR f.direction = 2 THEN 1 ELSE 0 END) AS output, " +
    "           SUM(CASE WHEN f.direction = 1 OR f.direction = 3 THEN 1 ELSE 0 END) AS input, " +
    "           COUNT(f.faculty) AS total_count " +
    "    FROM FormEntity f " +
    "    GROUP BY f.faculty " +
    "    ORDER BY COUNT(f.faculty) DESC " + 
    ") WHERE ROWNUM <= 5")
    List<MobilityFacultyProjection> getFacultyStatistics();
}
