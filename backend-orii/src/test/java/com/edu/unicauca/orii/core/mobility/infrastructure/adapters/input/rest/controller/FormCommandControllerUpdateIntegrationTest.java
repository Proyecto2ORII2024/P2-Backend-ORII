package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.IdentificationTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.PersonTypeEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.PersonData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request.EventRequest;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request.FormCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class FormCommandControllerUpdateIntegrationTest {

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

    @Autowired
    private ObjectMapper objectMapper;

    private final String ENDPOINT = "/form/update";

    private String toJson(FormCreateRequest data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

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
    public void testUpdateFormWithValidData() throws Exception {
        FormCreateRequest validData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.INCOMING_IN_PERSON)
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(PersonTypeEnum.STUDENT)
                        .firstName("Carlos")
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT + "/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(jsonPath("$.orii").value(true))
                .andExpect(jsonPath("$.direction").value(DirectionEnum.INCOMING_IN_PERSON.name()))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.cta").value(1))
                .andExpect(jsonPath("$.entryDate").value("30-09-2024"))
                .andExpect(jsonPath("$.exitDate").value("31-10-2024"))
                .andExpect(jsonPath("$.originProgram").value("Ingeniería de Sistemas"))
                .andExpect(jsonPath("$.destinationProgram").value("Ciencia de Datos"))
                .andExpect(jsonPath("$.city").value("Bogotá"))
                .andExpect(jsonPath("$.country").value("Colombia"))
                .andExpect(jsonPath("$.teacher").value("Dr. Juan Pérez"))
                .andExpect(jsonPath("$.faculty").value("Facultad de Ingeniería Electrónica y Telecomunicaciones"))
                .andExpect(jsonPath("$.funding").value(2000.00))
                .andExpect(jsonPath("$.fundingSource").value("Beca Colciencias"))
                .andExpect(jsonPath("$.destination").value("Universidad Nacional de Colombia"))
                .andExpect(jsonPath("$.origin").value("Universidad del Cauca"))
                .andExpect(jsonPath("$.agreement.agreementId").value(initialAgreementEntity.getAgreementId()))
                .andExpect(jsonPath("$.event.description").value("Congreso Internacional de Inteligencia Artificial"))
                .andExpect(jsonPath("$.event.eventType.eventTypeId").value(initialEventTypeEntity.getEventTypeId()))
                .andExpect(jsonPath("$.person.identificationType").value(IdentificationTypeEnum.CC.name()))
                .andExpect(jsonPath("$.person.personType").value(PersonTypeEnum.STUDENT.name()))
                .andExpect(jsonPath("$.person.firstName").value("Carlos"))
                .andExpect(jsonPath("$.person.lastName").value("Gómez"))
                .andExpect(jsonPath("$.person.identification").value("987654321"))
                .andExpect(jsonPath("$.person.email").value("carlos.gomez@unicauca.edu.co"));
    }

    @Test
    public void testUpdateFormWithEmptyOrri() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyGender() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("") // Campo gender vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyOriginProgram() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
                .originProgram("") // Campo originProgram vacío
                .destinationProgram("Ciencia de Datos")
                .city("Bogotá")
                .country("Colombia")
                .teacher("Dr. Juan Pérez")
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyDestinationProgram() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
                .originProgram("Ingeniería de Sistemas")
                .destinationProgram("") // Campo destinationProgram vacío
                .city("Bogotá")
                .country("Colombia")
                .teacher("Dr. Juan Pérez")
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyCity() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
                .originProgram("Ingeniería de Sistemas")
                .destinationProgram("Ciencia de Datos")
                .city("") // Campo city vacío
                .country("Colombia")
                .teacher("Dr. Juan Pérez")
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyCountry() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
                .originProgram("Ingeniería de Sistemas")
                .destinationProgram("Ciencia de Datos")
                .city("Bogotá")
                .country("") // Campo country vacío
                .teacher("Dr. Juan Pérez")
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyTeacher() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .teacher("") // Campo teacher vacío
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isOk());  // Expecting HTTP 200 OK
    }

    @Test
    public void testUpdateFormWithEmptyFaculty() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .faculty("") // Campo faculty vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyFundingSource() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .fundingSource("") // Campo fundingSource vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyDestination() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .destination("") // Campo destination vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyOrigin() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .origin("") // Campo origin vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyAgreementId() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                // Campo agreementId no incluido para simular que está vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyEventDescription() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("") // Campo event.description vacío
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyPersonFirstName() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(PersonTypeEnum.TEACHER)
                        .firstName("") // Campo person.firstName vacío
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyPersonLastName() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(PersonTypeEnum.TEACHER)
                        .firstName("Carlos")
                        .lastName("") // Campo person.lastName vacío
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyPersonIdentification() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                        .identification("") // Campo person.identification vacío
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }

    @Test
    public void testUpdateFormWithEmptyPersonEmail() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                        .email("") // Campo person.email vacío
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isOk());  // Expecting HTTP 200 OK
    }

    @Test
    public void testUpdateNonExistentForm() throws Exception {
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
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isNotFound());  // Expecting HTTP 404 Not Found
    }

    @Test
    public void testUpdateFormWithNullOrri() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(null) // Campo orii nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullDirection() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(null) // Campo direction nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullCta() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(null) // Campo cta nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullEntryDate() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(null) // Campo entryDate nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullExitDate() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
                .orii(true)
                .direction(DirectionEnum.OUTGOING_IN_PERSON)
                .gender("Male")
                .cta(1)
                .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
                .exitDate(null) // Campo exitDate nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullTeacher() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .teacher(null) // Campo teacher nulo
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isOk());  // Expecting HTTP 200 OK
    }
    @Test
    public void testUpdateFormWithNullFunding() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .funding(null) // Campo funding nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullAgreementId() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(null) // Campo agreementId nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullEventTypeId() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(null) // Campo eventTypeId nulo
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullPersonIdentificationType() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(null) // Campo identificationType nulo
                        .personType(PersonTypeEnum.TEACHER)
                        .firstName("Carlos")
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullPersonType() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                .agreementId(initialAgreementEntity.getAgreementId())
                .event(EventRequest.builder()
                        .description("Congreso Internacional de Inteligencia Artificial")
                        .eventTypeId(initialEventTypeEntity.getEventTypeId())
                        .build())
                .person(PersonData.builder()
                        .identificationType(IdentificationTypeEnum.CC)
                        .personType(null) // Campo personType nulo
                        .firstName("Carlos")
                        .lastName("Gómez")
                        .identification("987654321")
                        .email("carlos.gomez@unicauca.edu.co")
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());  // Expecting HTTP 400 Bad Request
    }
    @Test
    public void testUpdateFormWithNullPersonEmail() throws Exception {
        FormCreateRequest invalidData = FormCreateRequest.builder()
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
                        .email(null) // Campo email nulo
                        .build())
                .build();

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isOk());  // Expecting HTTP 200 OK
    }

    @Test
    public void testUpdateFormWithValidDirection() throws Exception {
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
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFormWithValidPersonIdentificationType() throws Exception {
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
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFormWithValidPersonPersonType() throws Exception {
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
                .faculty("Facultad de Ingeniería Electrónica y Telecomunicaciones")
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

        mockMvc.perform(put(ENDPOINT+"/{id}", initialFormEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isOk());
    }

}