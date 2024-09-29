package com.edu.unicauca.orii.core.mobility.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IAgreementCommandPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IAgreementCommandPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AgreementCommandService implements IAgreementCommandPort {
    
    private final IAgreementCommandPersistencePort persistencePort;

    @Override
    public Agreement createAgreement(Agreement agreement) {
        return persistencePort.createAgreement(agreement);
    }

    @Override
    public Agreement updateAgreement(Long id, Agreement agreement) {
        return persistencePort.updateAgreement(id, agreement);
    }
    
}
