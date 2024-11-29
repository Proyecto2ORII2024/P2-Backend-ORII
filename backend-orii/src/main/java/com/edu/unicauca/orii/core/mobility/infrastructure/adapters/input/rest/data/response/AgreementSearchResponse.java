package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.response;


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
public class AgreementSearchResponse {
    private Long agreementId;
    private String institution;
    private String agreementNumber;
    private String country;
}
