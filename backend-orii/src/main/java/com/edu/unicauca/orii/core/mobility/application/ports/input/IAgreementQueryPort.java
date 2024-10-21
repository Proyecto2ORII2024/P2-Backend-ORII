package com.edu.unicauca.orii.core.mobility.application.ports.input;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

/**
 * Interface for lookup operations in {@link Agreement}.
 * <p>
 * This interface provides methods to list agreements by pagination and list
 * agreements by filters, such as agreement number or agreement name. These
 * methods will be implemented in the services
 * </p>
 */
public interface IAgreementQueryPort {
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
     * The criteria for an agreement to be considered active are defined by the
     * business logic,
     * often determined by the status field or a similar attribute.
     * </p>
     * 
     * @return a {@link List} of {@link Agreement} entities that are currently
     *         active in the system.
     */
    List<Agreement> getActiveAgreements();
}
