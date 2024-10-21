package com.edu.unicauca.orii.core.mobility.application.ports.output;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

/**
 * Interface for persistence operations on {@link Agreement}.
 * <p>
 * This interface provides methods to create, delete, and update agreements in the persistence layer.
 * </p>
 */
public interface IAgreementCommandPersistencePort {

     /**
     * Persists a new {@link Agreement} in the database.
     * @param agreement The {@link Agreement} object representing the new agreement to be persisted.
     * @return The persisted {@link Agreement} object, potentially enriched with database-generated values.
     */
     public Agreement createAgreement(Agreement agreement);

     public void deleteAgreement(Long id);

     /**
     * Updates an existing {@link Agreement} in the database.
     * 
     * @param id The ID of the {@link Agreement} to be updated.
     * @param agreement The updated {@link Agreement} details.
     * @return The updated {@link Agreement}.
     */
     public Agreement updateAgreement(Long id, Agreement agreement);
}
