package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
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
public class EventRequest {

    @JsonIgnore
    private Long eventId;

    @NotBlank(message = "The description is required")
    private String description;
    
    @NotNull(message = "The description is required")
    @Min(value = 1, message = "eventType must be a positive number")
    private Long eventTypeId;
}
