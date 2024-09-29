package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.application.ports.output.IFormCommandPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Form;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.EventEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.EventTypeEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.PersonEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IEventAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IFormAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IPersonAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IAgreementRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IEventRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IEventTypeRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IPersonRepository;

import lombok.RequiredArgsConstructor;

/**
 *  Adapter class that implements the {@link IFormCommandPersistencePort} interface.
 * 
 *  @author JulianRuano
 *  @author NarenImbachi
 *  @author Juliana2101
 */
@Component
@RequiredArgsConstructor
public class FormCommandJpaAdapter implements IFormCommandPersistencePort {

        private final IFormRepository formRepository;
        private final IEventRepository eventRepository;
        private final IPersonRepository personRepository;
        private final IAgreementRepository agreementRepository;
        private final IEventTypeRepository eventTypeRepository;

        private final IFormAdapterMapper formAdapterMapper;
        private final IEventAdapterMapper eventAdapterMapper;
        private final IPersonAdapterMapper personAdapterMapper;


        /**
         * {@inheritDoc}
         * <p>This method creates a {@link Form} entity by converting it to a {@link FormEntity} and saving it to the database.</p>
         * 
         * @param form The {@link Form} entity to be created.
         * @return The created {@link Form} entity.
         * 
         */
        @Override
        public Form createForm(Form form) {

                AgreementEntity agreementEntity = agreementRepository
                        .findById(form.getAgreement().getAgreementId())
                        .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                        MessageLoader.getInstance().getMessage(MessagesConstant.EM002,"Agreement" ,form.getAgreement().getAgreementId())));

                FormEntity formEntity = formAdapterMapper.toFormEntity(form);
                formEntity.setAgreement(agreementEntity);

                EventEntity eventEntity = eventAdapterMapper.toEventEntity(form.getEvent());
                EventTypeEntity eventTypeEntity = eventTypeRepository
                                .findById(form.getEvent().getEventType().getEventTypeId())
                                .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                                MessageLoader.getInstance().getMessage(MessagesConstant.EM002,"EventType" ,form.getEvent().getEventType().getEventTypeId())));

                                
                eventEntity.setEventType(eventTypeEntity);
                eventEntity = eventRepository.save(eventEntity);
                formEntity.setEvent(eventEntity);

                PersonEntity personEntity = personAdapterMapper.toPersonEntity(form.getPerson());
                personEntity = personRepository.save(personEntity);
                formEntity.setPerson(personEntity);

                formEntity = formRepository.save(formEntity);
                return formAdapterMapper.toForm(formEntity);

        }

        @Override
        public Form updateForm(Long id, Form form) {
                FormEntity formEntity = formRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El formulario con ID " + id + " no fue encontrado"));

                if (formEntity.getAgreement().getAgreementId() != form.getAgreement().getAgreementId()) {
                        AgreementEntity agreementEntity = agreementRepository
                                        .findById(form.getAgreement().getAgreementId())
                                        .orElseThrow(() -> new IllegalArgumentException("Convenio no encontrado"));
                        formEntity.setAgreement(agreementEntity);
                }

                if (formEntity.getEvent().getEventType().getEventTypeId() != form.getEvent().getEventType()
                                .getEventTypeId()) {
                        EventTypeEntity eventTypeEntity = eventTypeRepository
                                        .findById(form.getEvent().getEventType().getEventTypeId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Tipo de evento no encontrado"));

                        formEntity.getEvent().setEventType(eventTypeEntity);
                }

                formEntity.setOrii(form.getOrii());
                formEntity.setDirection(form.getDirection());
                formEntity.setCta(form.getCta());
                formEntity.setEntryDate(form.getEntryDate());
                formEntity.setExitDate(form.getExitDate());
                formEntity.setOriginProgram(form.getOriginProgram());
                formEntity.setDestinationProgram(form.getDestinationProgram());
                formEntity.setCity(form.getCity());
                formEntity.setCountry(form.getCountry());
                formEntity.setTeacher(form.getTeacher());
                formEntity.setFaculty(form.getFaculty());
                formEntity.setFunding(form.getFunding());
                formEntity.setFundingSource(form.getFundingSource());
                formEntity.setDestination(form.getDestination());
                formEntity.setOrigin(form.getOrigin());
                
                formEntity.getPerson().setIdentificationType(form.getPerson().getIdentificationType());
                formEntity.getPerson().setPersonType(form.getPerson().getPersonType());
                formEntity.getPerson().setFirstName(form.getPerson().getFirstName());
                formEntity.getPerson().setLastName(form.getPerson().getLastName());
                formEntity.getPerson().setIdentification(form.getPerson().getIdentification());
                formEntity.getPerson().setEmail(form.getPerson().getEmail());

                // Convertir y devolver el Form actualizado
                return formAdapterMapper.toForm(formRepository.save(formEntity));
        }

    @Override
    public void deleteForm(Long id) {
        FormEntity formEntity = formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        formRepository.delete(formEntity);
    }

    @Override
    public List<Form> findAllForms() {
         List<FormEntity> formEntities = formRepository.findAll();
         return formEntities.stream()
            .map(formEntity -> formAdapterMapper.toForm(formEntity))
            .collect(Collectors.toList());
    }
}
