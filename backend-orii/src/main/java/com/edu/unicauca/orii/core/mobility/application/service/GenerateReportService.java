package com.edu.unicauca.orii.core.mobility.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IGenerateReportPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IExcelReportPersistencePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerateReportService implements IGenerateReportPort{

    private final IExcelReportPersistencePort excelReportPersistencePort;
    @Override
    public byte[] generateReportMobility() {
        return excelReportPersistencePort.generateExcelMobility();
    }
    
}
