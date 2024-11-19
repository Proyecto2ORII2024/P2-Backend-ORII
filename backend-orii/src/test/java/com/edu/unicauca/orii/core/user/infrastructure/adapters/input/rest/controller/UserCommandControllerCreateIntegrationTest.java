package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.service.EmailService;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCommonRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class UserCommandControllerCreateIntegrationTest {
    @MockBean
    protected EmailService emailService;

    @MockBean
    protected IEmailConfirmationOutput emailConfirmationOutput;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    private String ENDPOINT = "/users/create";

    private String toJson(UserCommonRequest data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserWithValidData() throws Exception {
        UserCommonRequest validData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepitoperez@unicauca.edu.co")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateUserWithNonAdminRole() throws Exception {
        UserCommonRequest validData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepitoperez@unicauca.edu.co")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validData)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateUserWithEmptyFaculty() throws Exception {
        String invalidRequest = """
                {
                    "faculty": "",
                    "email": "pepitoperez@unicauca.edu.co",
                    "password": "Pperepitoz123!",
                    "role": "ADMIN"
                }
                """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithEmptyEmail() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateUserWithEmptyRole() throws Exception {
        String invalidRequest = """
                {
                    "faculty": "FIET",
                    "email": "pepitoperez@unicauca.edu.co",
                    "password": "Pperepitoz123!",
                    "role": ""
                }
                """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithNullEmail() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email(null)
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithNullFaculty() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(null)
                .email("pepitoperez@unicauca.edu.co")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testCreateUserWithNullRole() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepitoperez@unicauca.edu.co")
                .role(null)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithInvalidEmailFormat() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepitoperezunicaucaeduco")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithInvalidEmailDomain() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepitoperez@gmail.com")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithInvalidRole() throws Exception {
        String invalidRequest = """
            {
                "faculty": "FIET",
                "email": "pepitoperez@unicauca.edu.co",
                "password": "Pperepitoz123!",
                "role": "CAMIONERO"
            }
            """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithInvalidFaculty() throws Exception {
        String invalidRequest = """
            {
                "faculty": "Facultad de ingeniería aeronautica",
                "email": "pepitoperez@unicauca.edu.co",
                "password": "Pperepitoz123!",
                "role": "ADMIN"
            }
            """;

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateUserWithUnsanitizedEmail() throws Exception {
        UserCommonRequest invalidData = UserCommonRequest.builder()
                .faculty(FacultyEnum.FIET)
                .email("pepito.perez+email@unicauca.edu.co")
                .role(RoleEnum.ADMIN)
                .build();

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidData)))
                .andExpect(status().isBadRequest());
    }






}