package com.edu.unicauca.orii.core.mobility.domain.model;
import java.util.Date;
import java.util.List;

import com.edu.unicauca.orii.core.mobility.domain.enums.ScopeEnum;
import com.edu.unicauca.orii.core.mobility.domain.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an agreement between institutions/Represents an agreement entity in the mobility system.
 * <p>
 * This class contains details about a specific agreement
 * including its identification, associated institution, agreement number, 
 * country, description, scope, start date, and related forms.
 * </p>
 * 
 * The class is annotated with Lombok annotations to reduce boilerplate code:
 * <ul>
 *   <li>{@link AllArgsConstructor} generates a constructor with all fields as parameters.</li>
 *   <li>{@link NoArgsConstructor} generates a default constructor.</li>
 *   <li>{@link Getter} and {@link Setter} generate getter and setter methods for all fields.</li>
 *   <li>{@link Builder} enables the builder pattern for object creation.</li>
 * </ul>
 * @author SergioAriza
 * @author RubenSantiagoCP
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/**
 * Represents an agreement entity in the mobility system.
 */
public class Agreement {
    
    /** The unique identifier for the agreement. */
    private Long agreementId;
    /** The name of the institution involved in the agreement. */
    private String institution;
    /** The agreement number. */
    private String agreementNumber;

    private String country;
    /** A brief description of the agreement. */
    private String description;

    private ScopeEnum scope;
    /** The start date of the agreement. */
    private Date startDate;

    /** A list of forms associated with the agreement. */
    private List<Form> forms;

    /**The current state of agreement */
    private StatusEnum status;
}
