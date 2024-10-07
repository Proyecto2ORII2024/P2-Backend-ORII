package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request;

import java.util.Date;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.PersonData;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormCreateRequest {
    
    @JsonIgnore
    private Long id;

    @NotNull(message = "The orii is required")
    private Boolean orii;

    @NotNull(message = "The address is required")
    private DirectionEnum direction;

    @NotBlank(message ="The gender is required")
    private String gender;

    @NotNull(message = "CTA is required")
    @Min(value = 1, message = "CTA must be a positive number")
    private Integer cta;

    @NotNull(message = "The entry date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date entryDate;

    @NotNull(message = "The exit date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date exitDate;

    @NotBlank(message = "The origin program is required")
    private String originProgram;

    @NotBlank(message = "The destination program is required")
    private String destinationProgram;

    @NotBlank(message = "The type of mobility is required")
    private String city;

    @NotBlank(message = "The country is required")
    private String country;

    private String teacher;

    @NotBlank(message = "The city is required")
    private String faculty;

    @NotNull(message = "The funding is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Funding must be a positive number")
    private BigDecimal funding;

    @NotBlank(message = "The funding source is required")
    private String fundingSource;

    @NotBlank(message = "The destination is required")
    private String destination;

    @NotBlank(message = "The origin is required")
    private String origin;

    private Long agreementId;
    
    @Valid
    private EventRequest event;
    @Valid
    private PersonData person;
}
