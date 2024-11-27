package com.edu.unicauca.orii.core.mobility.application.ports.output;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

/**
 * Interface for search operations on {@link Agreement}.
 * <p>
 * This interface provides methods to list agreements by pagination and list
 * agreements by filters, such as agreement number or agreement name. these
 * methods will be implemented in the adapters in the infrastructure layer.
 * </p>
 */
public interface IAgreementQueryPersistencePort {

    /**
     * list of {@link Page<Agreement>} of the database
     * <p>
     * 
     * @param pageable a send the size of the list and the page to list
     * @return list of {@link Page<Agreement>} Depending on the parameters sent, we
     *         will list a Pagination with one or many agreements
     */
    Page<Agreement> getAgreement(Pageable pageable);

    /**
     * list of {@link List<Agreement>} of the database
     * <p>
     * 
     * @param search A String type parameter is sent where it can contain the
     *               agreement number or the name of the agreement.
     * @return list of {@link List<Agreement>} Depending on the parameters, it will
     *         list one or many agreements
     */
    List<Agreement> getAgreementByNumberOrName(String search);

    /**
     * Retrieves a list of active agreements from the database.
     * <p>
     * This method returns all agreements that are currently active.
     * The definition of "active" is determined by the business logic in the
     * implementation (e.g., based on a status field).
     * </p>
     * 
     * @return a {@link List} of {@link Agreement} entities that are currently
     *         active.
     */
    List<Agreement> getAgreementsActives();

    List<Agreement> getAgreementsByCountry(String country);
}
