package com.edu.unicauca.orii.core.mobility.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IAgreementCommandPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IAgreementCommandPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing {@link Agreement} entities.
 * <p>
 * This class implements the {@link IAgreementCommandPort} interface and
 * provides methods
 * to create, update, and delete {@link Agreement} entities by delegating the
 * operations
 * to the {@link IAgreementCommandPersistencePort}.
 * </p>
 * 
 * <p>
 * It uses the {@link IAgreementCommandPersistencePort} for persistence-related
 * operations.
 * </p>
 * 
 * <p>
 * This class is annotated with {@link Service} to mark it as a Spring service
 * and
 * {@link RequiredArgsConstructor} to automatically generate the constructor for
 * final fields.
 * </p>
 * 
 * @author RubenSantiagoCP
 * @author Sergio
 * @author Carlos
 */

@Service
@RequiredArgsConstructor
public class AgreementCommandService implements IAgreementCommandPort {

    private final IAgreementCommandPersistencePort persistencePort;

    /**
     * {@inheritDoc}
     */
    @Override
    public Agreement createAgreement(Agreement agreement) {
        return persistencePort.createAgreement(agreement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Agreement updateAgreement(Long id, Agreement agreement) {
        return persistencePort.updateAgreement(id, agreement);
    }

    @Override
    public void deleteAgreement(Long id) {
        this.persistencePort.deleteAgreement(id);
    }

}
