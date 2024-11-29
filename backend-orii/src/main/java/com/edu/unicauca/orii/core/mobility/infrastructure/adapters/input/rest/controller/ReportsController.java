package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IGenerateReportPort;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")

public class ReportsController {
    private final IGenerateReportPort generateReportPort;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mobility")
    public ResponseEntity<byte[]> generateReportMobility() {
        byte[] excelFile = generateReportPort.generateReportMobility();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mobility.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);    
    }
}
