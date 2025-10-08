package com.example.petlifecycle.health.report.dto;

import com.example.petlifecycle.health.report.entity.HealthReport;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HealthReportDto {

    @Getter
    @NoArgsConstructor
    public static class CreateHealthReportRequest {
        private String surveyResult;
    }

    @Getter
    public static class HealthReportResponse {
        private Long id;
        private String reportContent;
        private LocalDateTime createdAt;

        public HealthReportResponse(Long id, String reportContent, LocalDateTime createdAt) {
            this.id = id;
            this.reportContent = reportContent;
            this.createdAt = createdAt;
        }

        public static HealthReportResponse from(HealthReport healthReport) {
            return new HealthReportResponse(
                    healthReport.getId(),
                    healthReport.getReportContent(),
                    healthReport.getCreatedAt()
            );
        }
    }
}
