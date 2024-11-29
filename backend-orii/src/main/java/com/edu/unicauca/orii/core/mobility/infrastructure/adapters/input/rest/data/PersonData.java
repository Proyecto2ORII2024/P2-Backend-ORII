package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data;

import com.edu.unicauca.orii.core.mobility.domain.enums.IdentificationTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.PersonTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonData {
    
    @JsonIgnore
    private Long personId;
    @NotNull(message = "The identificationTypeNum is required")
    private IdentificationTypeEnum identificationType;
    @NotNull(message = "The personType is required")
    private PersonTypeEnum personType;
    @NotBlank(message = "The firstName is required")  
    private String firstName;
    @NotBlank(message = "The lastName is required")
    private String lastName;
    @NotBlank(message = "The identification is required")
    private String identification;
    //The email can be null 
    private String email;
}
