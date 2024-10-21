package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.IdentificationTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.PersonTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.EventEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.EventTypeEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.PersonEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IEventTypeRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class FormCommandControllerDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IFormRepository formRepository;

    private FormEntity initialFormEntity;

    @Autowired
    private IAgreementRepository agreementRepository;

    private AgreementEntity initialAgreementEntity;

    @Autowired
    private IEventTypeRepository eventTypeRepository;

    private EventTypeEntity initialEventTypeEntity;

    @BeforeEach
    public void setup() throws ParseException {

        initialAgreementEntity = AgreementEntity.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new Date())  // Current date
                .build();

        initialAgreementEntity = agreementRepository.save(initialAgreementEntity);  // Save and get ID

        initialEventTypeEntity = EventTypeEntity.builder()
                .name("Evento Presencial")
                .build();

        initialEventTypeEntity = eventTypeRepository.save(initialEventTypeEntity); // Save and get ID

        // Create and save the initial FormEntity in the database before each test
        initialFormEntity = FormEntity.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
                .originProgram("Ingeniería de Sistemas")
                .destinationProgram("Ciencia de Datos")
                .city("Bogotá")
                .country("Colombia")
                .teacher("Dr. Juan Pérez")
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
                .funding(BigDecimal.valueOf(2000))
                .fundingSource("Beca Colciencias")
                .destination("Universidad Nacional de Colombia")
                .origin("Universidad del Cauca")
                .agreement(initialAgreementEntity)
                .event(EventEntity.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventType(initialEventTypeEntity)
                        .build())
                .person(PersonEntity.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(PersonTypeEnum.TEACHER)
                        .firstName("Carlos")
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        initialFormEntity = formRepository.save(initialFormEntity);  // Save and get ID

    }

    @Test
    public void testDeleteFormSuccessfully() throws Exception {
        // Delete the form successfully
        mockMvc.perform(delete("/form/delete/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());  // Expecting HTTP 204 No Content
    }

    @Test
    public void testDeleteFormThatDoesNotExist() throws Exception {
        // Attempt to delete a non-existent form
        mockMvc.perform(delete("/form/delete/{id}", 9999L)  // ID that doesn't exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting HTTP 404 Not Found
    }

    @Test
    public void testDeleteFormWithoutId() throws Exception {
        // Attempt to delete without providing an ID
        mockMvc.perform(delete("/form/delete/")  // Missing ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());  // Expecting HTTP 405 Method Not Allowed
    }

}