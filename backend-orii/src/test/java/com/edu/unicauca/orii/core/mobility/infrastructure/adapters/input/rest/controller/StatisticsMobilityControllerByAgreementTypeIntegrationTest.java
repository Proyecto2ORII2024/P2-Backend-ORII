package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.IdentificationTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.PersonTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.StatusEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.PersonData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request.EventRequest;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request.FormCreateRequest;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.EventTypeEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IEventTypeRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticsMobilityControllerByAgreementTypeIntegrationTest extends BaseTest {
    
    private final String ENDPOINT = "/statistics/mobilitybyevent";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IAgreementRepository agreementRepository;
    @Autowired
    private IFormRepository formRepository;
    @Autowired
    private IEventTypeRepository eventTypeRepository;

    private AgreementEntity initialAgreementEntity;
    private EventTypeEntity initialEventTypeEntity;

    private String toJson(FormCreateRequest data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

    @BeforeAll
    void setUp() {
        this.formRepository.deleteAll();
        this.agreementRepository.deleteAll();

        initialAgreementEntity = AgreementEntity.builder()
                .institution("Universidad Nacional")
                .agreementNumber("AC213")
                .status(StatusEnum.ACTIVE)
                .country("Colombia")
                .description("Intercambio")
                .scope(ScopeEnum.NATIONAL)
                .startDate(new Date())  // Current date
                .build();

        initialAgreementEntity = agreementRepository.save(initialAgreementEntity);  // Save and get ID

        initialEventTypeEntity = eventTypeRepository.findById(1L).orElseThrow();
    }

    @AfterEach
    void tearDown(){
        this.formRepository.deleteAll();
    }
    @AfterAll
    void tearDownAll(){
        this.agreementRepository.deleteAll();
    }

    @Test
    public void testGetMobilityByAgreementType() throws Exception {
        FormCreateRequest validData = FormCreateRequest.builder()
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
                .faculty(FacultyEnum.FIET)
                .funding(BigDecimal.valueOf(2000))
                .fundingSource("Beca Colciencias")
                .destination("Universidad Nacional de Colombia")
                .origin("Universidad del Cauca")
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(PersonTypeEnum.TEACHER)
                        .firstName("Carlos")
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(post("/form/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agreementType").exists())
                .andExpect(jsonPath("$.totalMobilityByAgreementsType").exists())
                .andExpect(jsonPath("$.agreementType").isArray())
                .andExpect(jsonPath("$.totalMobilityByAgreementsType").isArray())
                .andExpect(jsonPath("$.agreementType").isNotEmpty())
                .andExpect(jsonPath("$.totalMobilityByAgreementsType").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetMobilityByAgreementTypeWithoutPermission() throws Exception {
        mockMvc.perform(get(this.ENDPOINT)).andExpect(status().isForbidden());
    }




    
}
