package com.edu.unicauca.orii.core.mobility.application.ports.input;

import com.edu.unicauca.orii.core.mobility.domain.model.Form;

/**
 * Interface that defines writing methods for {@link Form} entity
 * @author JulianRuano
 */
public interface IFormCommandPort {
    /**
     * Create a new form
     * @param form The {@link Form} entity to be created
     * @return The created {@link Form} entity
     */
    public Form createForm(Form form);

    /**
     * Update an existing form
     * @param id The id of the {@link Form} entity to be updated
     * @param form The {@link Form} entity to be updated
     * @return The updated {@link Form} entity
     */
    public Form updateForm(Long id, Form form);

    /**
     * Delete an existing form
     * @param id The id of the {@link Form} entity to be deleted
     */
    public void deleteForm(Long id);
}
