package com.edu.unicauca.orii.core.mobility.application.ports.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.unicauca.orii.core.mobility.domain.model.Form;
/**
 * Interface for querying persistence Forms.

 *
 * @author JhossefCons
 */
public interface IFormQueryPersistencePort {
    /**
     * Retrieves a paginated list of all forms.
     * 
     * @param pageable the pagination information
     * @return a page of forms
     */
    Page<Form> findAllForms(Pageable pageable);

    Form findFormById(Long formId);
}
