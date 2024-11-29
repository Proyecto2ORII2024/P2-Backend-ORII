package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.mobility.application.service.AgreementQueryService;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.mapper.IAgreementRestMapper;

import lombok.RequiredArgsConstructor;

/**
 * Controller for agreement management.
 * This class provides endpoints to get all agreements in a paginated way and also get agreement either by agreement number or name, in the filter you can add *part of the agreement number, or also part of the name and it will look for the search considerations.
 * @author Brayan142002
 * 
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/agreement")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
public class AgreementQueryController {
    
    private final AgreementQueryService agreementQueryService;
    private final IAgreementRestMapper agreementRestMapper;

     /**
     * Gets all the conventions depending on the size and page we send it in the parameter
     *
     * @param pageable we send the size of elements that will have the page and the page number
     * @return ResponseEntity with the data of the agreements
     */
    @GetMapping("/allAgreements")
    public ResponseEntity<Page<AgreementData>> getAgreements(Pageable pageable) {
       Page<Agreement> objAgreement=agreementQueryService.getAgreement(pageable);
           Page<AgreementData> objResponse = objAgreement.map(agreementRestMapper::toAgreementData);
    
         return ResponseEntity.ok(objResponse);

    }

    /**
     * It obtains all the conventions depending on the parameter that we send, if it is by its code or name, it will look for considerations in the characters of the code or characters of the name of the convention.
     *
     * @param search we send either the code or part of it, or the name or part of the name and will search for matches
     * @return ResponseEntity with the data of the agreements or agreement that you send in the parameter
     */
    @GetMapping("filterbynumberorname/{search}")
    public ResponseEntity<List<AgreementData>> getAgreenmentByNumberOrName(@PathVariable String search) {
        List<Agreement> objAgreements=agreementQueryService.getAgreementByNumberOrName(search);
        List<AgreementData> objResponse=agreementRestMapper.toListAgreementData(objAgreements);
        return ResponseEntity.ok(objResponse);
    }
    
    /**
     * Retrieves a list of active agreements.
     * <p>
     * This method handles HTTP GET requests to the "/active" endpoint and returns all agreements that are currently active.
     * It calls the service layer to fetch active agreements and maps the result into a list of {@link AgreementData} to be returned in the response.
     * </p>
     *
     * @return ResponseEntity containing a {@link List} of {@link AgreementData} representing the active agreements.
     * The response has an HTTP status of 200 (OK) and includes the data of the active agreements.
     */
    @GetMapping("/all")
    public ResponseEntity<List<AgreementData>> getActivesAgreement(){
        List<Agreement> objAgreements = agreementQueryService.getActiveAgreements();
        List<AgreementData> objResponse = agreementRestMapper.toListAgreementData(objAgreements);
        return ResponseEntity.ok(objResponse);
    }

}
    
