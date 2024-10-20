package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data;

import java.util.Date;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class AgreementData {

    private Long agreementId;

    @NotBlank(message = "The name of institution is required")
    private String institution;

    @NotBlank(message = "The agreement number is required")
    private String agreementNumber;

    @NotBlank(message = "The agreement country is required")
    private String country;

    @NotBlank(message = "The agreement description is required")
    private String description;

    @NotNull(message = "The agreement scope is required")
    private ScopeEnum scope;

    @NotNull(message = "The entry date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @NotNull(message = "The status is required")
    private StatusEnum status;

}
