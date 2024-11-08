package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.service.EmailService;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class UserCommandControllerDeleteIntegrationTest {

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected IEmailConfirmationOutput emailConfirmationOutput;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;
    private UserEntity initialUserEntity;

    @BeforeEach
    public void setup() {
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
    public void testDeleteUserSuccessfully() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", initialUserEntity.getUserId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeleteUserThatDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserWithoutId() throws Exception {
        mockMvc.perform(delete("/users/delete/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}
