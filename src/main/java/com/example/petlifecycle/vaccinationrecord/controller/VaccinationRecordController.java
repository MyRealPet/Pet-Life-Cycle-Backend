package com.example.petlifecycle.vaccinationrecord.controller;

import com.example.petlifecycle.auth.service.AuthService;
import com.example.petlifecycle.vaccinationrecord.controller.request.RegisterVacRecordRequest;
import com.example.petlifecycle.vaccinationrecord.controller.request.UpdateVacRecordRequest;
import com.example.petlifecycle.vaccinationrecord.controller.response.ReadVacRecordResponse;
import com.example.petlifecycle.vaccinationrecord.entity.VaccinationRecord;
import com.example.petlifecycle.vaccinationrecord.service.VaccinationRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet/{petId}/vac-record")
public class VaccinationRecordController {

    private final VaccinationRecordService vaccinationRecordService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> registerVaccinationRecord(/*@RequestHeader("Authorization")String authorizedHeader,*/@PathVariable Long petId, @RequestBody RegisterVacRecordRequest request) {
        //Long accountId = authService.getAccountIdFromToken(authorizedHeader);
        Long accountId = 1001L;

        try {
            log.info("Registering vaccination record for account {}", accountId);
            vaccinationRecordService.registerVacRecord(accountId, petId, request);
            return ResponseEntity.ok("접종 이력 등록 성공 accountId: "+ accountId + ", petId: " + petId);
        } catch (Exception e) {
            log.error("접종 이력 등록 실패: {}", e.getMessage());
            throw new RuntimeException("접종 이력 등록에 실패했습니다. accountId: "+ accountId + ", petId: " + petId);
        }
    }

    // 수정 시, 기록을 불러오기 위함
    @GetMapping("/{recordId}")
    public ResponseEntity<ReadVacRecordResponse> readVaccinationRecord(@PathVariable Long petId, @PathVariable Long recordId) {
        Long accountId = 1001L;
        try {
            log.info("Reading vaccination record for account {}", accountId);
            ReadVacRecordResponse response = vaccinationRecordService.readVacRecord(accountId, petId, recordId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("{} 접종 이력 조회 실패: {}",recordId, e.getMessage());
            throw new RuntimeException(recordId +" 접종 이력 조회에 실패했습니다. accountId: "+ accountId + ", petId: " + petId);
        }
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<String> updateVaccinationRecord(/*@RequestHeader("Authorization")String authorizedHeader,*/
                                                          @PathVariable Long petId, @PathVariable Long recordId, @RequestBody UpdateVacRecordRequest request) {
//        Long accountId = authService.getAccountIdFromToken(authorizedHeader);
        Long accountId = 1001L;
        try {
            log.info("Updating vaccination record for account {}", accountId);
            vaccinationRecordService.updateVaccinationRecord(accountId, petId, recordId, request);
            return ResponseEntity.ok("접종 이력 수정 완료 accountId: "+ accountId + ", petId: " + petId);
        } catch (Exception e) {
            log.error("접종 이력 수정 실패: {}", e.getMessage());
            throw new RuntimeException("접종 이력 수정에 실패했습니다. accountId: "+ accountId + ", petId: " + petId);
        }
    }

}
