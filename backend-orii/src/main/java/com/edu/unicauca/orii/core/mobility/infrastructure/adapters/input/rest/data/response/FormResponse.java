package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.response;

import java.util.Date;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.PersonData;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponse {
    
    private Long id;

    private Boolean orii;

    private DirectionEnum direction;

    private String gender;

    private Integer cta;

    private Date entryDate;

    private Date exitDate;

    private String originProgram;

    private String destinationProgram;

    private String city;

    private String country;

    private String teacher;

    private String faculty;

    private BigDecimal funding;

    private String fundingSource;

    private String destination;

    private String origin;


    private AgreementData agreement;
    private EventResponse event;
    private PersonData person;
}
