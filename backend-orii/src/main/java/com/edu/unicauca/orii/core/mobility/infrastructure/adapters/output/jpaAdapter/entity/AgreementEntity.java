package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity;

import java.util.Date;
import java.util.List;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agreements")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgreementEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agreement_seq")
    @SequenceGenerator(name = "agreement_seq", sequenceName = "agreement_seq", allocationSize = 1)
    @Column(name = "id_agreement")
    private Long agreementId;

    @Column(nullable = false, length = 255)
    private String institution;

    @Column(nullable = false, length = 255)
    private String agreementNumber;

    @Column(nullable = false, length = 255)
    private String country;

    @Column(nullable = false, length = 1024)
    private String description;

    @Column(name="agreement_scope", nullable = false, length = 24)
    private ScopeEnum scope;

    @Column(nullable = false)
    private Date startDate;

    // Relationship with Form
    @OneToMany(mappedBy = "agreement")
    private List<FormEntity> forms;

    @Column(nullable = false, length = 8)
    private StatusEnum status;
}
