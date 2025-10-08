package com.example.petlifecycle.ai.controller;

import com.example.petlifecycle.ai.dto.AiQuestionRequest;
import com.example.petlifecycle.ai.dto.AiResponse;
import com.example.petlifecycle.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AiController {

    private final AiService aiService;

    @PostMapping("/health-reports/{reportId}/ask-ai")
    public ResponseEntity<AiResponse> getAiAnswer(
            @PathVariable String reportId,
            @RequestBody AiQuestionRequest request) {
        Long parsedReportId;
        try {
            String numericPart = reportId.substring(reportId.lastIndexOf('-') + 1);
            parsedReportId = Long.parseLong(numericPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid report ID format: " + reportId);
        }

        AiResponse response = aiService.getAnswer(parsedReportId, request.getQuestion());
        return ResponseEntity.ok(response);
    }
}
