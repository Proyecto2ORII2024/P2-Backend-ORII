package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "forms")
public class FormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_seq")
    @SequenceGenerator(name = "form_seq", sequenceName = "form_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private Boolean orii;

    @Column(length = 255)
    private DirectionEnum direction;

    @Column(length = 100)
    private String gender;

    private Integer cta;

    @Temporal(TemporalType.DATE)
    private Date entryDate;

    @Temporal(TemporalType.DATE)
    private Date exitDate;

    @Column(length = 255)
    private String originProgram;

    @Column(length = 255)
    private String destinationProgram;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(length = 255)
    private String teacher;

    @Column(length = 150)
    private String faculty;

    @Column(precision = 10, scale = 2)
    private BigDecimal funding;

    @Column(length = 255)
    private String fundingSource;

    @Column(length = 255)
    private String destination;

    @Column(length = 255)
    private String origin;
    
    // Relationships with Agreement, Event, and Person
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_event")
    private EventEntity event;
    
    @ManyToOne
    @JoinColumn(name = "id_agreement", nullable = true)
    private AgreementEntity agreement;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_person")
    private PersonEntity person;
   
}
