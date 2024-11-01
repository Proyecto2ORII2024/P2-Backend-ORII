package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserCommandControllerUpdateIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    private UserEntity initialUserEntity;

    @Autowired
    private ObjectMapper objectMapper;

    private final String END_POINT = "/users/update/";
    private final String DATE_TEST = "23-08-2024";
    private final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

    private String toJSON(UserData data) throws Exception{
        return this.objectMapper.writeValueAsString(data);
    }

    private boolean validationPassword(String password){
        return password.matches(this.REGEX);
    }

    @BeforeEach
    public void setUp() throws ParseException{

        Date dateTest = new SimpleDateFormat("dd-MM-yyyy").parse(this.DATE_TEST);

        UserEntity user = new UserEntity();

        user.setUserId(1L);
        user.setFaculty(FacultyEnum.FIET);
        user.setEmail("user@unicauca.edu.co");
        user.setPassword("SecurePassword123!");
        user.setRole(RoleEnum.USER);
        user.setUpdatePassword(dateTest);

        this.initialUserEntity = this.userRepository.save(user);
    }

    @Test
    public void testUpdateUserWithValidData() throws Exception{
        UserData validData = new UserData();
        Date dateUpdatePassword = new SimpleDateFormat("dd-MM-yyyy").parse(new Date().toString());
        
        validData.setFaculty(FacultyEnum.FIET);
        validData.setEmail("user1@unicauca.edu.co");
        validData.setRole(RoleEnum.ADMIN);

        this.mockMvc.perform(
            put(this.END_POINT+"/{id}", this.initialUserEntity.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.toJSON(validData))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("faculty").value("FIET"))
        .andExpect(jsonPath("email").value("user1@unicauca.edu.co"))
        .andExpect(jsonPath("role").value("ADMIN"))
        .andExpect(jsonPath("updatePassword").value(dateUpdatePassword));
    }

    @Test
    public void testUpdateUserWithEmptyFaculty() throws Exception{
        UserData invalidData = new UserData();
        Date dateUpdatePassword = new SimpleDateFormat("dd-MM-yyyy").parse(new Date().toString());
        
        invalidData.setFaculty(FacultyEnum.FIET);
        invalidData.setEmail("user1@unicauca.edu.co");
        invalidData.setRole(RoleEnum.ADMIN);
    }

    @Test
    public void testUpdateUserWithEmptyEmail() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithEmptyPassword() throws Exception{
        //TO DO
    }

    @Test
    public void testUpdateUserWithEmptyRole() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithNullFaculty() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithNullEmail() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithNullPassword() throws Exception{
        //TO DO
    }

    @Test
    public void testUpdateUserWithNullRole() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithInvalidEmail() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithWrongEmailDomain() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithInvalidRole() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithInvalidPassword() throws Exception{
        //TO DO
    }

    @Test
    public void testUpdateUserWithInvalidFaculty() throws Exception{
        
    }

    @Test
    public void testUpdateUserWithUnsanitisedEmail() throws Exception{
        
    }

}
