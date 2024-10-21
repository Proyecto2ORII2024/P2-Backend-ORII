package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AgreementCommandControllerCreateIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String ENDPOINT = "/agreement/create";

    private String toJson(AgreementData data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

    @Test
    public void testCreateAgreementWithValidData() throws Exception {
        AgreementData validData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isCreated());  // Expecting HTTP 201 for success
    }

    @Test
    public void testCreateAgreementWithEmptyInstitution() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 for bad request
    }

    @Test
    public void testCreateAgreementWithEmptyAgreementNumber() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithEmptyCountry() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithEmptyDescription() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithEmptyScope() throws Exception {
        String invalidRequest = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "",
                    "startDate": "23-08-2024"
                }
                """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithEmptyStartDate() throws Exception {
        String invalidRequest = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "NATIONAL",
                    "startDate": ""
                }
                """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateAgreementWithNullInstitution() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution(null)
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithNullAgreementNumber() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber(null)
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithNullCountry() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country(null)
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithNullDescription() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description(null)
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithNullScope() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(null)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithNullStartDate() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(null)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAgreementWithValidScopeInternational() throws Exception {
        AgreementData validData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.INTERNATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isCreated());

    }

    @Test
    public void testCreateAgreementWithInvalidScopeHumano() throws Exception {
        String invalidRequest = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "HUMANO",
                    "startDate": "23-08-2024"
                }
                """;

        mockMvc.perform(post("/agreement/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 for bad request
    }


}