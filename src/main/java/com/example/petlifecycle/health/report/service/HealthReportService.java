package com.example.petlifecycle.health.report.service;

import com.example.petlifecycle.global.ai.OpenAIService;
import com.example.petlifecycle.health.report.dto.HealthReportDto;
import com.example.petlifecycle.health.report.entity.HealthReport;
import com.example.petlifecycle.health.report.repository.HealthReportRepository;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.repository.PetAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthReportService {

    private final OpenAIService openAIService;
    private final HealthReportRepository healthReportRepository;
    private final PetAccountRepository petAccountRepository;

    public HealthReportDto.HealthReportResponse createHealthReport(Long petId, HealthReportDto.CreateHealthReportRequest request) {
        PetAccount petAccount = petAccountRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID: " + petId));

        String prompt = createHealthReportPrompt(petAccount, request.getSurveyResult());
        String reportContent = openAIService.getChatResponse(prompt).block();

        HealthReport healthReport = new HealthReport(petAccount, reportContent);
        healthReportRepository.save(healthReport);

        return HealthReportDto.HealthReportResponse.from(healthReport);
    }

    private String createHealthReportPrompt(PetAccount petAccount, String surveyResult) {
        // Here you can create a more detailed prompt based on the pet's information and the survey result.
        return "Create a health report for my pet, " + petAccount.getName() + ".\n" +
                "Here is the survey result:\n" + surveyResult;
    }
}
