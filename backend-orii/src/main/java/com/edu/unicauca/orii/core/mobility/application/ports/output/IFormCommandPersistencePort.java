package com.edu.unicauca.orii.core.mobility.application.ports.output;

import java.util.List;

import com.edu.unicauca.orii.core.mobility.domain.model.Form;

public interface IFormCommandPersistencePort {
    public Form createForm(Form form);
    public Form updateForm(Long id, Form form);
    public void deleteForm(Long id);
    public List<Form> findAllForms();
}
