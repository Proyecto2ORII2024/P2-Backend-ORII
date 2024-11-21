package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository;

import java.util.List;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityAgreementTypeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityFacultyProjection;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityTrendProjection;

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

   @Query("SELECT EXTRACT(YEAR FROM f.entryDate) AS years, COUNT(f) AS amountMobility " +
       "FROM FormEntity f " +
       "WHERE EXTRACT(YEAR FROM f.entryDate) >= EXTRACT(YEAR FROM CURRENT_DATE) - 8 " +
       "GROUP BY EXTRACT(YEAR FROM f.entryDate) " +
       "ORDER BY EXTRACT(YEAR FROM f.entryDate) DESC")
        List<MobilityTrendProjection> getAnnualMobilityTrend();

   /*@Query("SELECT et.name AS eventType, COUNT(DISTINCT f.id) AS totalFormsByEvent " +
       "FROM FormEntity f " +
       "JOIN EventEntity e ON f.event = e.eventId " +
           "JOIN EventType et ON e.eventId = et.eventTypeId " +
       "GROUP BY et.name")*/
    @Query("SELECT e.eventType.name AS agreementType, COUNT(DISTINCT f) AS totalAgreementType " +
       "FROM FormEntity f " +
       "JOIN f.event e " +
       "GROUP BY e.eventType.name")
   List<MobilityAgreementTypeProjection> getDistributionByTypeOfAgreement();
}
