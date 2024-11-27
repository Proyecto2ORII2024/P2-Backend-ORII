package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;

public interface IAgreementRepository extends JpaRepository<AgreementEntity, Long> {

    @Query("SELECT a FROM AgreementEntity a WHERE UPPER(a.agreementNumber) LIKE UPPER(CONCAT('%', :search, '%') )"+
    "OR UPPER(a.institution) LIKE UPPER(CONCAT('%', :search, '%'))")
    List<AgreementEntity>findByNumberOrName(@Param("search")String search);
    
    @Query("SELECT a FROM AgreementEntity a WHERE a.status = 0")
    List<AgreementEntity>findActiveAgreements();

    @Query("SELECT a FROM Agreement a WHERE a.country = :country")
    List<AgreementEntity> findByCountry(@Param("country") String country);
}
