package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AgreementCommandControllerDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAgreementRepository agreementRepository;
    private AgreementEntity initialAgreementEntity;


    @BeforeEach
    public void setup() {
        // Create and save the initial AgreementEntity in the database before each test
        initialAgreementEntity = AgreementEntity.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.INTERNATIONAL)
                .startDate(new Date())  // Current date
                .build();

        initialAgreementEntity = agreementRepository.save(initialAgreementEntity);  // Save and get ID
    }

    @Test
    public void testDeleteAgreementSuccessfully() throws Exception {
        // Delete the agreement successfully
        mockMvc.perform(delete("/agreement/delete/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());  // Expecting HTTP 204 No Content
    }

    @Test
    public void testDeleteAgreementThatDoesNotExist() throws Exception {
        // Attempt to delete a non-existent agreement
        mockMvc.perform(delete("/agreement/delete/{id}", 9999L)  // ID that doesn't exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expecting HTTP 404 Not Found
    }

    @Test
    public void testDeleteAgreementWithoutId() throws Exception {
        // Attempt to delete without providing an ID
        mockMvc.perform(delete("/agreement/delete/")  // Missing ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());  // Expecting HTTP 405 Method Not Allowed
    }

}