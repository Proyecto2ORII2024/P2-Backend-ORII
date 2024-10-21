package com.edu.unicauca.orii.core.mobility.application.ports.input;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

/**
 * Interface for the agreement command operations.
 * <p>
 * This interface provides methods to handle the creation, deletion,
 * and update operations for agreements in the system. 
 * The {@link Agreement} model is used as the primary entity for these operations.
 * </p>
 * 
 * @author SergioAriza
 */
public interface IAgreementCommandPort {

    /**
     * Creates a new {@link Agreement} in the system.
     * @param agreement The {@link Agreement} object representing the new agreement to be created.
     * @return The created {@link Agreement} object, including any system-generated values.
     */

    public Agreement createAgreement(Agreement agreement);
    
    public void deleteAgreement(Long id);
    
    /**
     * Updates an existing {@link Agreement} by its ID.
     * 
     * @param id        The ID of the {@link Agreement} to be updated.
     * @param agreement The {@link Agreement} object containing the updated data.
     * @return The updated {@link Agreement}.
     */
    public Agreement updateAgreement(Long id, Agreement agreement);
}
