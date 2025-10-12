package com.example.petlifecycle.medicalrecord.controller;

import com.example.petlifecycle.medicalrecord.controller.request.RegisterMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet/{petId}/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<String> registerMedicalRecord(@PathVariable("petId") Long petId, @RequestBody @Valid RegisterMedicalRecordRequest request) {
        Long accountId = 1001L;
        try {
            medicalRecordService.registerMedicalRecord(accountId, petId, request);
            return ResponseEntity.ok("진료기록이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("진료기록 등록 실패: {}", e.getMessage());
            throw new RuntimeException("진료기록 등록에 실패했습니다.");
        }
    }


