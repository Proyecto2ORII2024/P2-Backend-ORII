package com.edu.unicauca.orii.core.mobility.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IFormCommandPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IFormCommandPersistencePort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IFormFormatterResultOutputPort;
import com.edu.unicauca.orii.core.mobility.domain.enums.DirectionEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.PersonTypeEnum;
import com.edu.unicauca.orii.core.mobility.domain.model.Form;

import lombok.RequiredArgsConstructor;
/**
 * Service implementation for managing {@link Form} entities.
 * <p>
 * This class implements the {@link IFormCommandPort} interface and provides methods 
 * to create, update, and delete {@link Form} entities by delegating the operations 
 * to the {@link IFormCommandPersistencePort}.
 * </p>
 * 
 * <p>It uses the {@link IFormCommandPersistencePort} for persistence-related operations.</p>
 * 
 * <p>
 * This class is annotated with {@link Service} to mark it as a Spring service and
 * {@link RequiredArgsConstructor} to automatically generate the constructor for final fields.
 * </p>
 * @author JulianRuano
 */
@Service
@RequiredArgsConstructor
public class FormCommandService implements IFormCommandPort {

    private final IFormCommandPersistencePort persistencePort;
    private final IFormFormatterResultOutputPort formFormatterResultOutputPort;
    /**
     * {@inheritDoc}
     * <p>This method delegates the creation of a {@link Form} to the persistence layer.</p>
     * <p>It also validates that a teacher is required for incoming students.</p> 
     * <p>If the teacher is not provided, an error message is returned.</p>
     * <p>If the person is not a student, the teacher is set to Not applicable.</p>
     * @param form The form to be created.
     * @return The created form.
     * @see Form
     * @see IFormCommandPersistencePort
     * @see IFormFormatterResultOutputPort
     * @see PersonTypeEnum
     * @see DirectionEnum
     */
    @Override
    public Form createForm(Form form) {
 
        if (form.getPerson() != null && form.getPerson().getPersonType() != null && form.getPerson().getPersonType().equals(PersonTypeEnum.STUDENT)) {
            if (form.getDirection().equals(DirectionEnum.INCOMING_IN_PERSON) || form.getDirection().equals(DirectionEnum.INCOMING_VIRTUAL)) {
                if (form.getTeacher() == null || form.getTeacher().isEmpty()) {
                    formFormatterResultOutputPort.returnResponseErrorTeacherRequired(
                        "The teacher is required because he/she is a student with Incoming Academic Mobility");
                }
            }
        }else{
            form.setTeacher("N.A.");
        }

        return persistencePort.createForm(form);
    }

    /**
     * {@inheritDoc}
     * <p>This method delegates the updating of a {@link Form} to the persistence layer.</p>
     */
    @Override
    public Form updateForm(Long id, Form form) {
        if (form.getPerson() != null && form.getPerson().getPersonType() != null && form.getPerson().getPersonType().equals(PersonTypeEnum.STUDENT)) {
            if (form.getDirection().equals(DirectionEnum.INCOMING_IN_PERSON) || form.getDirection().equals(DirectionEnum.INCOMING_VIRTUAL)) {
                if (form.getTeacher() == null || form.getTeacher().isEmpty()) {
                    formFormatterResultOutputPort.returnResponseErrorTeacherRequired(
                        "The teacher is required because he/she is a student with Incoming Academic Mobility");
                }
            }
        }else{
            form.setTeacher("N.A.");
        }
        
        return persistencePort.updateForm(id, form);
    }

    /**
     * {@inheritDoc}
     * <p>This method delegates the deletion of a {@link Form} to the persistence layer.</p>
     */
    @Override
    public void deleteForm(Long id) {
     persistencePort.deleteForm(id);
    }
}
