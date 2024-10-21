package com.edu.unicauca.orii.core.mobility.domain.model;

import java.math.BigDecimal;
import java.util.Date;

import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Form {

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
    
    private Agreement agreement;
    private Event event;
    private Person person;
}
