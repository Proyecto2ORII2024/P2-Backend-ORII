package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.application.ports.output.IAgreementCommandPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IAgreementAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Adapter class that implements the {@link IAgreementCommandPersistencePort}
 * interface.
 * 
 * @author Ruben SantiagoCp
 * @author SergioAriza
 */
@Component
@RequiredArgsConstructor

public class AgreementCommandJpaAdapter implements IAgreementCommandPersistencePort {

    private final IAgreementRepository agreementRepository;

    private final IAgreementAdapterMapper agreementAdapterMapper;

    /**
     * {@inheritDoc}
     * <p>
     * This method creates a {@link Agreement} entity by converting it to a
     * {@link AgreementEntity} and saving it to the database.
     * </p>
     * 
     * @param form The {@link Agreement} entity to be created.
     * @return The created {@link Agreement} entity.
     * 
     */
    @Override
    public Agreement createAgreement(Agreement agreement) {
        return agreementAdapterMapper
                .toAgreement(agreementRepository.save(agreementAdapterMapper.toAgreementEntity(agreement)));
    }

    @Override
    public void deleteAgreement(Long id) {
        Optional<AgreementEntity> agreementEntity = this.agreementRepository.findById(id);
        if (agreementEntity.isEmpty()) {
            throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                    MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "Agreement", id));
        }
        this.agreementRepository.deleteById(id);

    }

    public Agreement updateAgreement(Long id, Agreement agreement) {
        if (!agreementRepository.existsById(id)) {
            throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                    MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "Agreement", id));
        }

        agreement.setAgreementId(id);

        return agreementAdapterMapper
                .toAgreement(agreementRepository.save(agreementAdapterMapper.toAgreementEntity(agreement)));
    }

}
