package com.example.petlifecycle.health.report.controller;

import com.example.petlifecycle.health.report.dto.HealthReportDto;
import com.example.petlifecycle.health.report.service.HealthReportService;
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
