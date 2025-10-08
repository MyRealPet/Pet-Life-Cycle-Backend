package com.example.petlifecycle.healthreport.controller;

import com.example.petlifecycle.healthreport.dto.HealthReportDto;
import com.example.petlifecycle.healthreport.service.HealthReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/petlifecycle/health-report")
@RequiredArgsConstructor
public class HealthReportController {

    private final HealthReportService healthReportService;

    @PostMapping("/{petId}")
    public ResponseEntity<HealthReportDto.HealthReportResponse> createHealthReport(
            @PathVariable Long petId,
            @RequestBody HealthReportDto.CreateHealthReportRequest request) {
        HealthReportDto.HealthReportResponse response = healthReportService.createHealthReport(petId, request);
        return ResponseEntity.ok(response);
    }
}
