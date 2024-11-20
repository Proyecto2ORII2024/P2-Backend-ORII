package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IStatisticsMobilityPort;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityTrend;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class StatisticsMobilityController {
    
    private final IStatisticsMobilityPort statisticsMobilityPort;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/faculty")
    public ResponseEntity<MobilityFaculty> getStatisticsByFaculty() {
        MobilityFaculty mobilityFaculty = statisticsMobilityPort.getStatisticsByFaculty();
        return ResponseEntity.ok(mobilityFaculty);     
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mobilitytrend")
    public ResponseEntity<MobilityTrend> getAnnualMobilityTrend(){
        MobilityTrend mobilityTrend=this.statisticsMobilityPort.getAnnualMobilityTrend();
        return ResponseEntity.ok(mobilityTrend);
    }

}
