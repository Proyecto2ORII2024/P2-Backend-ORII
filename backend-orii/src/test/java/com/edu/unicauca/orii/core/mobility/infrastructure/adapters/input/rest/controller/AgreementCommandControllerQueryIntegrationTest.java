package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.mapper.IAgreementRestMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IAgreementAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AgreementCommandControllerQueryIntegrationTest {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private IAgreementRepository agreementRepository;

    @Autowired
    private IFormRepository formRepository;
    
    @Autowired
    private IAgreementRestMapper agreementRestMapper;

    @Autowired
    private IAgreementAdapterMapper agreementAdapterMapper;

    private List<AgreementData> lstAgreementsData = new ArrayList<>();

    private final String AGREEMENT_NUMBER = "29";
    private final String AGREEMENT_INSTITUTION = "Buenos Aires";
    private final String DATE_TEST = "23-08-2024";

    private final String END_POINT = "/agreement";

    @BeforeAll
    public void setUp() throws ParseException{
        this.formRepository.deleteAll();
        this.agreementRepository.deleteAll();
        Date dateTest = new SimpleDateFormat("dd-MM-yyyy").parse(this.DATE_TEST);

        AgreementEntity initialAgreementEntity1 = AgreementEntity.builder()
                            .institution("Instituto Tecnológico")
                            .agreementNumber("IT85629")
                            .country("México")
                            .description("Programa de Doble Titulación")
                            .scope(ScopeEnum.INTERNATIONAL)
                            .startDate(dateTest)
                            .build();

        AgreementEntity initialAgreementEntity2 = AgreementEntity.builder()
                        .institution("Escuela Politécnica de Buenos Aires Andalucia")
                        .agreementNumber("EP629")
                        .country("España")
                        .description("Intercambio Cultural")
                        .scope(ScopeEnum.NATIONAL)
                        .startDate(dateTest)
                        .build();

        AgreementEntity initialAgreementEntity3 = AgreementEntity.builder()
                        .institution("Universidad de Buenos Aires")
                        .agreementNumber("UB100")
                        .country("Argentina")
                        .description("Becas de Investigación")
                        .scope(ScopeEnum.NATIONAL)
                        .startDate(dateTest)
                        .build();

        this.agreementRepository.saveAll(List.of(initialAgreementEntity1,initialAgreementEntity2,initialAgreementEntity3));

    
        this.lstAgreementsData = this.agreementRestMapper.toListAgreementData(
            this.agreementRepository.
            findAll().
            stream().
            map(agreementAdapterMapper::toAgreement).
            collect(Collectors.toList())
        );
    }
    
    @Test
    public void testGetgetAgreements() throws Exception{

        

        ResultActions response = this.mockMvc.perform(get(this.END_POINT+"/all"))
        .andExpect(status().isOk());

        for(int i=0; i< this.lstAgreementsData.size(); i++){
            response
            .andExpect(jsonPath("$.content["+i+"].institution").value(this.lstAgreementsData.get(i).getInstitution()))
            .andExpect(jsonPath("$.content["+i+"].agreementNumber").value(this.lstAgreementsData.get(i).getAgreementNumber()))
            .andExpect(jsonPath("$.content["+i+"].country").value(this.lstAgreementsData.get(i).getCountry()))
            .andExpect(jsonPath("$.content["+i+"].description").value(this.lstAgreementsData.get(i).getDescription()))
            .andExpect(jsonPath("$.content["+i+"].scope").value(this.lstAgreementsData.get(i).getScope().toString()))
            .andExpect(jsonPath("$.content["+i+"].startDate").value(this.DATE_TEST));
        }
    }
    
    @Test
    public void testGetAgreenmentByNumber() throws Exception {
        this.mockMvc.perform(get(this.END_POINT+"/filterbynumberorname/{search}",this.AGREEMENT_NUMBER)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetNoExistAgreenmentByNumber() throws Exception {
        this.mockMvc.perform(get(this.END_POINT+"/filterbynumberorname/{search}","AB")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAgreenmentByName() throws Exception {
        this.mockMvc.perform(get(this.END_POINT+"/filterbynumberorname/{search}",this.AGREEMENT_INSTITUTION)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetNoExistAgreenmentByName() throws Exception {
        this.mockMvc.perform(get(this.END_POINT+"/filterbynumberorname/{search}","Bogotá")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
}
