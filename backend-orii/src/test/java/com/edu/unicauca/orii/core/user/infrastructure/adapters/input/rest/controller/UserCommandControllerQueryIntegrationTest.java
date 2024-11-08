package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.service.EmailService;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper.IUserAdapterMapper;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class UserCommandControllerQueryIntegrationTest {

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected IEmailConfirmationOutput emailConfirmationOutput;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRestMapper userRestMapper;

    @Autowired
    private IUserAdapterMapper userAdapterMapper;

    private List<UserData> lstUsersData = new ArrayList<>();

    private final String END_POINT = "/users";

    @BeforeAll
    public void setUp() throws ParseException {
        this.userRepository.deleteAll();
        String DATE_TEST = "06-11-2024";
        Date dateTest = new SimpleDateFormat("dd-MM-yyyy").parse(DATE_TEST);

        // Crear usuarios de prueba
        UserEntity user1 = new UserEntity();
        user1.setEmail("pepitoperez1@unicauca.edu.co");
        user1.setRole(RoleEnum.ADMIN);
        user1.setFaculty(FacultyEnum.FIET);
        user1.setPassword("password");
        user1.setEmailVerified(true);
        user1.setUpdatePassword(dateTest);

        UserEntity user2 = new UserEntity();
        user2.setEmail("pepitoperez2@unicauca.edu.co");
        user2.setRole(RoleEnum.USER);
        user2.setFaculty(FacultyEnum.FIC);
        user2.setPassword("password");
        user2.setEmailVerified(true);
        user2.setUpdatePassword(dateTest);

        this.userRepository.saveAll(List.of(user1, user2));

        // Convertir a UserData para la verificación
        this.lstUsersData = this.userRestMapper.toUserDataList(
                this.userRepository.findAll()
                        .stream()
                        .map(userAdapterMapper::toUser)
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void testGetUsers() throws Exception {
        // Realiza una solicitud GET para obtener todos los usuarios y verifica que la respuesta sea 200 OK
        ResultActions response = this.mockMvc.perform(get(this.END_POINT + "/get"))
                .andExpect(status().isOk());

        // Verificar cada usuario en la respuesta JSON
        for (int i = 0; i < this.lstUsersData.size(); i++) {
            response
                    .andExpect(jsonPath("$[" + i + "].email").value(this.lstUsersData.get(i).getEmail()))
                    .andExpect(jsonPath("$[" + i + "].role").value(this.lstUsersData.get(i).getRole().toString()))
                    .andExpect(jsonPath("$[" + i + "].faculty").value(this.lstUsersData.get(i).getFaculty().getDisplayName()));
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        // Obtener un ID de usuario existente
        Long userId = this.userRepository.findAll().get(0).getUserId();

        // Realizar una solicitud GET para obtener el usuario por ID y verificar que la respuesta sea 200 OK
        ResultActions response = this.mockMvc.perform(get(this.END_POINT + "/get/{id}", userId))
                .andExpect(status().isOk());

        // Datos esperados del usuario
        UserData expectedUser = this.lstUsersData.get(0);

        // Verificar los detalles específicos del usuario en la respuesta JSON
        response
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
                .andExpect(jsonPath("$.role").value(expectedUser.getRole().toString()))
                .andExpect(jsonPath("$.faculty").value(expectedUser.getFaculty().getDisplayName()));
    }

    @Test
    public void testGetNonExistentUserById() throws Exception {
        // Realizar una solicitud GET para un ID inexistente y esperar 404 Not Found
        this.mockMvc.perform(get(this.END_POINT + "/get/{id}", 999L))
                .andExpect(status().isNotFound());
    }

}