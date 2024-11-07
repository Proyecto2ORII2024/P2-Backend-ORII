package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.service.EmailService;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username="admin", roles={"ADMIN"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserCommandControllerUpdateIntegrationTest {
    @MockBean
    protected EmailService emailService;

    @MockBean
    protected IEmailConfirmationOutput emailConfirmationOutput;
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    private UserEntity initialUserEntity;

    @Autowired
    private ObjectMapper objectMapper;

    private final String END_POINT = "/users/update/";
    private final String DATE_TEST = "23-08-2024";

    private String toJSON(UserData data) throws Exception{
        return this.objectMapper.writeValueAsString(data);
    }

    private Date getDateTest() throws ParseException{
        return new SimpleDateFormat("dd-MM-yyyy").parse(this.DATE_TEST);
    }

    @BeforeEach
    public void setUp(){
        this.userRepository.deleteAll();
        UserEntity user = new UserEntity();
        
        user.setFaculty(FacultyEnum.FIET);
        user.setEmail("user@unicauca.edu.co");
        user.setPassword("User1234!");
        user.setRole(RoleEnum.USER);
        user.setEmailVerified(true);
        user.setUpdatePassword(new Date(4000000));

        this.initialUserEntity = this.userRepository.save(user);
    }

    @Test
    public void testUpdateUserWithValidData() throws Exception{
        UserData validData = new UserData();
        
        validData.setFaculty(FacultyEnum.FIET);
        validData.setEmail("user1@unicauca.edu.co");
        validData.setRole(RoleEnum.ADMIN);

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.toJSON(validData))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("faculty").value("Facultad de ingeniería electrónica y telecomunicaciones"))
        .andExpect(jsonPath("email").value("user1@unicauca.edu.co"))
        .andExpect(jsonPath("role").value("ADMIN"))
        .andExpect(jsonPath("updatePassword").value(this.getDateTest().toString()));
    }

    @Test
    public void testUpdateUserWithEmptyFaculty() throws Exception{
        String invalidData = """
                {
                    "faculty":"",
                    "email":"user1@unicauca.edu.co",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithEmptyEmail() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería electrónica y telecomunicaciones",
                    "email":"",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithEmptyRole() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería electrónica y telecomunicaciones",
                    "email":"user1@unicauca.edu.co",
                    "role":""
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithNullFaculty() throws Exception{
        String invalidData = """
                {
                    "faculty":,
                    "email":"user1unicaucaeduco",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithNullEmail() throws Exception{
        UserData validData = new UserData();
        
        validData.setFaculty(FacultyEnum.FIET);
        validData.setRole(RoleEnum.ADMIN);

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.toJSON(validData))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithNullRole() throws Exception{
        UserData validData = new UserData();
        
        validData.setFaculty(FacultyEnum.FIET);
        validData.setEmail("user1@unicauca.edu.co");

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.toJSON(validData))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithInvalidEmail() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería electrónica y telecomunicaciones",
                    "email":"user1unicaucaeduco",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithWrongEmailDomain() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería electrónica y telecomunicaciones",
                    "email":"user1@gmail.com",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithInvalidRole() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería electrónica y telecomunicaciones",
                    "email":"user1@gmail.com",
                    "role":"CAMIONERO"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithInvalidFaculty() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería aeronautica",
                    "email":"user1@gmail.com",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithUnsanitisedEmail() throws Exception{
        String invalidData = """
                {
                    "faculty":"Facultad de ingeniería aeronautica",
                    "email":"u.ser+1@gmail.com",
                    "role":"ADMIN"
                }
                """;

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidData)
        )
        .andExpect(status().isBadRequest());
    }

}
