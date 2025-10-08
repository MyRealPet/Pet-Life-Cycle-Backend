package com.example.petlifecycle.healthreport.repository;

import com.example.petlifecycle.healthreport.entity.HealthReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
}
