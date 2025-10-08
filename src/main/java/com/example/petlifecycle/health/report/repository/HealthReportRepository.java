package com.example.petlifecycle.health.report.repository;

import com.example.petlifecycle.health.report.entity.HealthReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {
}
