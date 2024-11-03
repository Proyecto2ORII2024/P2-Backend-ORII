package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.StatusEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AgreementCommandControllerUpdateIntegrationTest extends BaseTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAgreementRepository agreementRepository;

    private AgreementEntity initialAgreementEntity;

    @Autowired
    private ObjectMapper objectMapper;

    private String ENDPOINT = "/agreement/update";

    private String toJson(AgreementData data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }
    @BeforeEach
    public void setup() {
        // Create and save the initial AgreementEntity in the database before each test
        initialAgreementEntity = AgreementEntity.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new Date())  // Current date
                .status(StatusEnum.ACTIVE)
                .build();

        initialAgreementEntity = agreementRepository.save(initialAgreementEntity);  // Save and get ID
    }


    @Test
    public void testUpdateAgreementWithValidData() throws Exception {
        AgreementData validData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk())// Expecting HTTP 200 OK
                .andExpect(jsonPath("$.institution").value("Universidad Nacional"))
                .andExpect(jsonPath("$.agreementNumber").value("AC213"))
                .andExpect(jsonPath("$.country").value("Colombia"))
                .andExpect(jsonPath("$.description").value("Intercambio"))
                .andExpect(jsonPath("$.scope").value("NATIONAL"))
                .andExpect(jsonPath("$.startDate").value("23-08-2024"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    public void testUpdateAgreementWithEmptyInstitution() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyAgreementNumber() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyCountry() throws Exception {
        // Country field is empty
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyDescription() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();


        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyScope() throws Exception {
        // Scope field is empty
        String updatedAgreement = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "",
                    "startDate": "23-08-2024",
                    "status": "INACTIVE"
                }
                """;

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgreement))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyStartDate() throws Exception {
        // Start date is empty
        String updatedAgreement = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "NATIONAL",
                    "startDate": "".
                    "status": "INACTIVE"
                }
                """;

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgreement))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateAgreementWithEmptyStatus() throws Exception {
        String updatedAgreement = """
            {
                "institution": "Universidad Nacional",
                "agreementNumber": "AC213",
                "country": "Colombia",
                "description": "Intercambio",
                "scope": "NATIONAL",
                "startDate": "23-08-2024",
                "status": ""
            }
            """;

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgreement))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateNonExistentAgreement() throws Exception {
        AgreementData validData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Un Cambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isNotFound());  // Expecting HTTP 404 Not FOund
    }


    @Test
    public void testUpdateAgreementWithNullInstitution() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution(null)
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullAgreementNumber() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber(null)
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullCountry() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country(null)
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullDescription() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description(null)
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullScope() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(null)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullStartDate() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(null)
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithNullStatus() throws Exception {
        AgreementData invalidData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(null)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAgreementWithValidScopeInternational() throws Exception {
        AgreementData validData = AgreementData.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.INTERNATIONAL)
                .startDate(new SimpleDateFormat("dd-MM-yyyy").parse("23-08-2024"))
                .status(StatusEnum.INACTIVE)
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAgreementWithInvalidScopeHumano() throws Exception {
        String updatedAgreement = """
                {
                    "institution": "Universidad Nacional",
                    "agreementNumber": "AC213",
                    "country": "Colombia",
                    "description": "Intercambio",
                    "scope": "HUMANO",
                    "startDate": "23-08-2024",
                    "status": "INACTIVE"
                }
                """;

        mockMvc.perform(put("/agreement/update/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgreement))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateAgreementWithInvalidStatus() throws Exception {
        String updatedAgreement = """
            {
                "institution": "Universidad Nacional",
                "agreementNumber": "AC213",
                "country": "Colombia",
                "description": "Intercambio",
                "scope": "NATIONAL",
                "startDate": "23-08-2024",
                "status": "INVALID_STATUS"
            }
            """;

        mockMvc.perform(put(ENDPOINT+"/{id}", initialAgreementEntity.getAgreementId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgreement))
                .andExpect(status().isBadRequest());
    }

}