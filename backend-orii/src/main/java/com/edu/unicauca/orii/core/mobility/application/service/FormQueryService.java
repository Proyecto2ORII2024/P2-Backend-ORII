package com.edu.unicauca.orii.core.mobility.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IFormQueryPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IFormQueryPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Form;

import lombok.RequiredArgsConstructor;

/**
 * Service class for querying forms.
 * This class provides methods for retrieving forms from the database.
 *
 * @author JhossefCons
 */
@Service
@RequiredArgsConstructor
public class FormQueryService implements IFormQueryPort{

        private final IFormQueryPersistencePort persistencePort;
        /**
         * Retrieves a paginated list of all forms.
         * 
         * @param pageable the pagination information
         * @return a page of forms
         */
        @Override
        public Page<Form> findAllForms(Pageable pageable) {
            return persistencePort.findAllForms(pageable);
        }
        @Override
        public Form findFormById(Long formId) {
            return persistencePort.findFormById(formId);
        }
    }