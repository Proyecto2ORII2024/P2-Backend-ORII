package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.edu.unicauca.orii.core.auth.filter.JwtFilter;
import com.edu.unicauca.orii.core.auth.util.IJwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin@unicauca.edu.co", roles = {"USER"})
@TestInstance(Lifecycle.PER_CLASS)
public class FormCommandControllerCreateIntegrationTest extends BaseTest{

  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private IAgreementRepository agreementRepository;

  @Autowired
  private IEventTypeRepository eventTypeRepository;

  private AgreementEntity initialAgreementEntity;

  private EventTypeEntity initialEventTypeEntity;

  @Autowired
  private IUserRepository userRepository;

  private UserEntity initialUserEntity;

  @Autowired
  JwtFilter jwtFilter;

  JwtFilter spyJwtFilter;



  private final String ENDPOINT = "/form/create";

  private String toJson(FormCreateRequest data) throws Exception {
      return objectMapper.writeValueAsString(data);
  }
  
  @BeforeAll
    public void setupAll() {
        UserEntity user = new UserEntity();
        
        user.setFaculty(FacultyEnum.FIET);
        user.setEmail("admin@unicauca.edu.co");
        user.setPassword("User1234!");
        user.setRole(RoleEnum.USER);
        user.setEmailVerified(true);
        user.setUpdatePassword(new Date(4000000));

        this.initialUserEntity = this.userRepository.save(user);
                
    }

    @BeforeEach
    public void mockUserEmail(){
      spyJwtFilter = spy(jwtFilter);

      when(spyJwtFilter.getEmail()).thenReturn(initialUserEntity.getEmail());
    }

  @BeforeEach
    public void setup() {
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


        initialEventTypeEntity = EventTypeEntity.builder()
                .name("Evento Presencial")
                .build();


        initialEventTypeEntity = eventTypeRepository.save(initialEventTypeEntity);

    }



  @Test
  public void testCreateFormWithValidData() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(validData)))
        .andExpect(status().isCreated());
  }
  
  @Test
  public void testCreateFormWithEmptyOrri() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera que falle por el campo orii vacío.
  }

  @Test
  public void testCreateFormWithEmptyGender() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por gender vacío.
  }

  @Test
  public void testCreateFormWithEmptyOriginProgram() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por originProgram vacío.
  }
  @Test
  public void testCreateFormWithEmptyDestinationProgram() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por destinationProgram vacío.
  }
  @Test
  public void testCreateFormWithEmptyCity() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por city vacío.
  }
  @Test
  public void testCreateFormWithEmptyCountry() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por country vacío.
  }
  @Test
  public void testCreateFormWithEmptyTeacherWithDirectionIncomingInPersonAndPersonTypeStudent() throws Exception {
    FormCreateRequest invalidData = FormCreateRequest.builder()
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
        .teacher("")
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithEmptyTeacherWithDirectionIncomingVirtualAndPersonTypeStudent() throws Exception {
    FormCreateRequest invalidData = FormCreateRequest.builder()
        .orii(true)
        .direction(DirectionEnum.INCOMING_VIRTUAL)
        .gender("Male")
        .cta(1)
        .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
        .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
        .originProgram("Ingeniería de Sistemas")
        .destinationProgram("Ciencia de Datos")
        .city("Bogotá")
        .country("Colombia")
        .teacher("")
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithNullTeacherWithDirectionIncomingInPersonAndPersonTypeStudent() throws Exception {
    FormCreateRequest invalidData = FormCreateRequest.builder()
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
        .teacher(null)
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithNullTeacherWithDirectionIncomingVirtualAndPersonTypeStudent() throws Exception {
    FormCreateRequest invalidData = FormCreateRequest.builder()
        .orii(true)
        .direction(DirectionEnum.INCOMING_VIRTUAL)
        .gender("Male")
        .cta(1)
        .entryDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2024"))
        .exitDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-10-2024"))
        .originProgram("Ingeniería de Sistemas")
        .destinationProgram("Ciencia de Datos")
        .city("Bogotá")
        .country("Colombia")
        .teacher(null)
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithEmptyTeacher() throws Exception {
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
        .teacher("")
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isCreated());
  }
  @Test
  public void testCreateFormWithNullTeacher() throws Exception {
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
        .teacher(null)
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
            .personType(PersonTypeEnum.STUDENT)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isCreated());
  }

  @Test
  public void testCreateFormWithEmptyFaculty() throws Exception {
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
        .faculty(null) // Campo faculty vacío
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por faculty vacío.
  }
  @Test
  public void testCreateFormWithEmptyFundingSource() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por fundingSource vacío.
  }
  @Test
  public void testCreateFormWithEmptyDestination() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por destination vacío.
  }
  @Test
  public void testCreateFormWithEmptyOrigin() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por origin vacío.
  }
  @Test
  public void testCreateFormWithEmptyAgreementId() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por agreementId vacío o nulo.
  }
  @Test
  public void testCreateFormWithEmptyEventDescription() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por event.description vacío.
  }
  @Test
  public void testCreateFormWithEmptyPersonFirstName() throws Exception {
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
            .firstName("") // Campo person.firstName vacío
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por person.firstName vacío.
  }
  @Test
  public void testCreateFormWithEmptyPersonLastName() throws Exception {
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
            .lastName("") // Campo person.lastName vacío
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por person.lastName vacío.
  }
  @Test
  public void testCreateFormWithEmptyPersonIdentification() throws Exception {
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
            .identification("") // Campo person.identification vacío
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por person.identification vacío.
  }
  @Test
  public void testCreateFormWithEmptyPersonEmail() throws Exception {
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
            .email("") // Campo person.email vacío
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isCreated()); // Se espera un 201 por person.email vacío.
  }
  @Test
  public void testCreateFormWithNullOrri() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por orii nulo.
  }
  @Test
  public void testCreateFormWithNullDirection() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por direction nulo.
  }
  @Test
  public void testCreateFormWithNullCta() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por cta nulo.
  }
  @Test
  public void testCreateFormWithNullEntryDate() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por entryDate nulo.
  }
  @Test
  public void testCreateFormWithNullExitDate() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por exitDate nulo.
  }
  
  @Test
  public void testCreateFormWithNullFunding() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por funding nulo.
  }
  @Test
  public void testCreateFormWithNullAgreementId() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por agreementId nulo.
  }
  @Test
  public void testCreateFormWithNullEventTypeId() throws Exception {
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
        .faculty(FacultyEnum.FIET)
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por eventTypeId nulo.
  }
  @Test
  public void testCreateFormWithNullPersonIdentificationType() throws Exception {
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
            .identificationType(null) // Campo identificationType nulo
            .personType(PersonTypeEnum.TEACHER)
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por identificationType nulo.
  }
  @Test
  public void testCreateFormWithNullPersonType() throws Exception {
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
            .personType(null) // Campo personType nulo
            .firstName("Carlos")
            .lastName("Gómez")
            .identification("987654321")
            .email("carlos.gomez@unicauca.edu.co")
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isBadRequest()); // Se espera un 400 por personType nulo.
  }
  @Test
  public void testCreateFormWithNullPersonEmail() throws Exception {
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
            .email(null) // Campo email nulo
            .build())
        .build();

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(invalidData)))
        .andExpect(status().isCreated()); // Se espera un 201 por email nulo.
  }
  @Test
  public void testCreateFormWithValidDirection() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(validData)))
        .andExpect(status().isCreated());
  }
  @Test
  public void testCreateFormWithInvalidDirection() throws Exception {
    String invalidRequest = """
        {
            "orii": true,
            "direction": "OUTGOING_ONLINE",
            "gender": "Male",
            "cta": 1,
            "entryDate": "30-09-2024",
            "exitDate": "31-10-2024",
            "originProgram": "Ingeniería de Sistemas",
            "destinationProgram": "Ciencia de Datos",
            "city": "Bogotá",
            "country": "Colombia",
            "teacher": "Dr. Juan Pérez",
            "faculty": FacultyEnum.FIET,
            "funding": 2000,
            "fundingSource": "Beca Colciencias",
            "destination": "Universidad Nacional de Colombia",
            "origin": "Universidad del Cauca",
            "agreementId": 1,
            "event": {
              "description": "Congreso Internacional de Inteligencia Artificial",
              "eventTypeId": 1
            },
            "person": {
              "identificationType": "CC",
              "personType": "TEACHER",
              "firstName": "Carlos",
              "lastName": "Gómez",
              "identification": "987654321",
              "email": "carlos.gomez@unicauca.edu.co"
            }
          }
          """;

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidRequest))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithValidPersonIdentificationType() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(validData)))
        .andExpect(status().isCreated());
  }
  @Test
  public void testCreateFormWithInvalidPersonIdentificationType() throws Exception {
    String invalidRequest = """
        {
            "orii": true,
            "direction": "OUTGOING_IN_PERSON",
            "gender": "Male",
            "cta": 1,
            "entryDate": "30-09-2024",
            "exitDate": "31-10-2024",
            "originProgram": "Ingeniería de Sistemas",
            "destinationProgram": "Ciencia de Datos",
            "city": "Bogotá",
            "country": "Colombia",
            "teacher": "Dr. Juan Pérez",
            "faculty": FacultyEnum.FIET,
            "funding": 2000,
            "fundingSource": "Beca Colciencias",
            "destination": "Universidad Nacional de Colombia",
            "origin": "Universidad del Cauca",
            "agreementId": 1,
            "event": {
              "description": "Congreso Internacional de Inteligencia Artificial",
              "eventTypeId": 1
            },
            "person": {
              "identificationType": "XYZ",
              "personType": "TEACHER",
              "firstName": "Carlos",
              "lastName": "Gómez",
              "identification": "987654321",
              "email": "carlos.gomez@unicauca.edu.co"
            }
          }
          """;

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidRequest))
        .andExpect(status().isBadRequest());
  }
  @Test
  public void testCreateFormWithValidPersonPersonType() throws Exception {
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

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(validData)))
        .andExpect(status().isCreated());
  }
  @Test
  public void testCreateFormWithInvalidPersonPersonType() throws Exception {
    String invalidRequest = """
        {
            "orii": true,
            "direction": "OUTGOING_IN_PERSON",
            "gender": "Male",
            "cta": 1,
            "entryDate": "30-09-2024",
            "exitDate": "31-10-2024",
            "originProgram": "Ingeniería de Sistemas",
            "destinationProgram": "Ciencia de Datos",
            "city": "Bogotá",
            "country": "Colombia",
            "teacher": "Dr. Juan Pérez",
            "faculty": FacultyEnum.FIET,
            "funding": 2000,
            "fundingSource": "Beca Colciencias",
            "destination": "Universidad Nacional de Colombia",
            "origin": "Universidad del Cauca",
            "agreementId": 1,
            "event": {
              "description": "Congreso Internacional de Inteligencia Artificial",
              "eventTypeId": 1
            },
            "person": {
              "identificationType": "CC",
              "personType": "RESEARCHER",
              "firstName": "Carlos",
              "lastName": "Gómez",
              "identification": "987654321",
              "email": "carlos.gomez@unicauca.edu.co"
            }
          }
          """;

    mockMvc.perform(post(ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidRequest))
        .andExpect(status().isBadRequest());
  }
}
