package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.edu.unicauca.orii.core.mobility.application.service.AgreementCommandService;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.mapper.IAgreementRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for managing agreements.
 * This class provides endpoints to create, update, and delete agreements.
 * @author SergioAriza
 * @author RubenSantiagoCP
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/agreement")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class AgreementCommandController {

    private final AgreementCommandService agreementCommandService;
    private final IAgreementRestMapper agreementRestMapper;

    /**
     * Creates a new agreement based on the provided request data.
     *
     * @param agreementCreateRequest the request containing agreement data to create
     * @return ResponseEntity containing the created agreement data
     */
    @PostMapping("/create")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_ADMIN')")
=======
    @PreAuthorize("hasRole('ADMIN')")
>>>>>>> 15ec0dfde18e16bc8ecbb97a7223405e4b1a78c3
    public ResponseEntity<AgreementData> createAgreement(@Valid 
            @RequestBody AgreementData agreementCreateRequest) {

        Agreement agreement = agreementRestMapper.toAgreement(agreementCreateRequest);
        agreement = agreementCommandService.createAgreement(agreement);
        return ResponseEntity.created(null).body(agreementRestMapper.toAgreementData(agreement));
    }

    /**
     * Updates an existing agreement by its ID.
     * 
     * @param id The ID of the agreement to be updated.
     * @param agreementUpdateRequest The updated data for the agreement.
     * @return A response entity containing the updated agreement data.
     */
    @PutMapping("/update/{id}")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_ADMIN')")
=======
    @PreAuthorize("hasRole('ADMIN')")
>>>>>>> 15ec0dfde18e16bc8ecbb97a7223405e4b1a78c3
    public ResponseEntity<AgreementData> updateAgreement(
            @PathVariable Long id, 
            @Valid @RequestBody AgreementData agreementUpdateRequest) {

        Agreement agreement = agreementRestMapper.toAgreement(agreementUpdateRequest);
        agreement = agreementCommandService.updateAgreement(id, agreement);
        return ResponseEntity.ok(agreementRestMapper.toAgreementData(agreement));
    }

    /**
     * Deletes an agreement by its ID.
     * 
     * @param id The ID of the agreement to be deleted.
     * @return A response entity with no content.
     */

    @DeleteMapping("/delete/{id}")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_ADMIN')")
=======
    @PreAuthorize("hasRole('ADMIN')")
>>>>>>> 15ec0dfde18e16bc8ecbb97a7223405e4b1a78c3
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long id) {
        agreementCommandService.deleteAgreement(id);
        return ResponseEntity.noContent().build();
    }

}
