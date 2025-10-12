package com.example.petlifecycle.medicalrecord.controller;

import com.example.petlifecycle.medicalrecord.controller.request.ListMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.controller.request.RegisterMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.controller.request.UpdateMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.controller.response.ListMedicalRecordResponse;
import com.example.petlifecycle.medicalrecord.controller.response.ReadMedicalRecordResponse;
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

    @GetMapping
    public ResponseEntity<ListMedicalRecordResponse> listMedicalRecord(@PathVariable("petId") Long petId, @ModelAttribute ListMedicalRecordRequest request) {
        Long accountId = 1001L;
        try {
            ListMedicalRecordResponse response = medicalRecordService.listMedicalRecord(accountId, petId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("진료기록 목록 조회 실패: {}", e.getMessage());
            throw new RuntimeException("진료기록 목록 조회에 실패했습니다.");
        }
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ReadMedicalRecordResponse> readMedicalRecord(@PathVariable("petId") Long petId, @PathVariable Long recordId) {
        try {
            Long accountId = 1001L;
            ReadMedicalRecordResponse response = medicalRecordService.readMedicalRecord(accountId, petId, recordId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("{} 진료기록 조회 실패: {}", recordId, e.getMessage());
            throw new RuntimeException("진료기록 조회에 실패했습니다.");
        }
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<String> updateMedicalRecord(@PathVariable("petId") Long petId, @PathVariable Long recordId,
                                                                   @RequestBody @Valid UpdateMedicalRecordRequest request) {
        Long accountId = 1001L;
        try {
            medicalRecordService.updateMedicalRecord(accountId, petId, recordId, request);
            return ResponseEntity.ok("진료기록 수정에 성공하셨습니다.");
        } catch (Exception e) {
            log.error("{} 진료기록 수정 실패: {}", recordId, e.getMessage());
            throw new RuntimeException("진료기록 수정에 실패하셨습니다.");
        }
    }
