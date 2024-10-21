package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.common.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.common.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.common.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IAgreementQueryPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IAgreementAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;

import lombok.RequiredArgsConstructor;


/**
 *  Adapter class that implements the {@link IAgreementQueryPersistencePort} interface.
 * 
 *  @author Brayan142002
 * 
 */
@Component
@RequiredArgsConstructor
public class AgreementQueryJpaAdapter implements IAgreementQueryPersistencePort{

    private final IAgreementRepository agreementRepository;
    private final IAgreementAdapterMapper agreementAdapterMapper;

    /**
         * {@inheritDoc}
         * <p>This method lists the agreements that are saved in the database, we bring a list of entities {@link AgreementEntity} and we map it to the domain{@link Agreement}</p>
         * 
         * @param pageable We send the page we want to show and the size of the list
         * @return returns a list {@Link Page<Agreement>}, the number of elements that will be displayed will be according to the parameters sent
         * 
         */
    @Override
    public Page<Agreement> getAgreement(Pageable pageable) {

     Page<AgreementEntity> agreementEntities=agreementRepository.findAll(pageable);
     if(agreementEntities.getContent().isEmpty()){
        throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                        MessageLoader.getInstance().getMessage(MessagesConstant.EM014,"Agreement" ));
     }
        return agreementEntities.map(agreementAdapterMapper::toAgreement);
     
    }

     /**
         * {@inheritDoc}
         * <p>This method lists the agreements through AgrementNumber or institution that are saved in the database, we bring a list of entities {@link AgreementEntity} and we map it to the domain{@link Agreement}</p>
         * 
         * @param search  We send the agreement number or the name of the institution 
         * @return returns a list {@Link List<Agreement>}, the number of elements that will be displayed will be according to the provided parameters (agreementNumber, Institution)
         * 
         */

    @Override
    public List<Agreement> getAgreementByNumberOrName(String search){
        List<AgreementEntity> agreementEntities=agreementRepository.findByNumberOrName(search);
        if(agreementEntities.isEmpty()){
              throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                        MessageLoader.getInstance().getMessage(MessagesConstant.EM014,"Agreement" ));
        }

        List<Agreement> listAgreement=agreementAdapterMapper.toAgreementList(agreementEntities);
        return listAgreement;
    }
    
}
