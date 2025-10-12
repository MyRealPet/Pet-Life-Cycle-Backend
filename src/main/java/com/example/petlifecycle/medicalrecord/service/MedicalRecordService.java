package com.example.petlifecycle.medicalrecord.service;

import com.example.petlifecycle.medicalrecord.controller.dto.FileInfoDto;
import com.example.petlifecycle.medicalrecord.controller.dto.MedicationItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TestItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TreatmentItemDto;
import com.example.petlifecycle.medicalrecord.controller.request.ListMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.controller.request.RegisterMedicalRecordRequest;
import com.example.petlifecycle.medicalrecord.controller.response.ListMedicalRecordResponse;
import com.example.petlifecycle.medicalrecord.entity.*;
import com.example.petlifecycle.medicalrecord.repository.*;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.repository.MetaDataFileRepository;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.service.PetAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordService {

    private final PetAccountService petAccountService;
    private final FileService fileService;
    private final MetaDataFileRepository metaDataFileRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final TestItemRepository testItemRepository;
    private final TreatmentItemRepository treatmentItemRepository;
    private final MedicationItemRepository medicationItemRepository;
    private final MedicalRecordAttachmentRepository medicalRecordAttachmentRepository;


    public void registerMedicalRecord(Long accountId, Long petId, RegisterMedicalRecordRequest request) {
        petAccountService.validateAndGetPetAccount(petId, accountId);
        MedicalRecord medicalRecord = medicalRecordRepository.save(request.toMedicalRecord(petId, accountId));
        Long medicalRecordId = medicalRecord.getId();

        if (request.getTestItems() != null) {
            request.getTestItems().stream()
                    .map(dto -> dto.toTestItem(medicalRecordId))
                    .forEach(testItemRepository::save);
        }

        // 처치내역
        if (request.getTreatmentItems() != null) {
            request.getTreatmentItems().stream()
                    .map(dto -> dto.toTreatmentItem(medicalRecordId))
                    .forEach(treatmentItemRepository::save);
        }

        // 처방내역
        if (request.getMedicationItems() != null) {
            request.getMedicationItems().stream()
                    .map(dto -> dto.toMedicationItem(medicalRecordId))
                    .forEach(medicationItemRepository::save);
        }

        // 첨부파일
        if (request.getAttachmentFileIds() != null) {
            request.getAttachmentFileIds().stream()
                    .map(fileId -> new MedicalRecordAttachment(medicalRecordId, fileId))
                    .forEach(medicalRecordAttachmentRepository::save);
        }

        log.info("진료기록 등록 완료: recordId={}, petId={}, accountId={}", medicalRecordId, petId, accountId);
    }

    public ListMedicalRecordResponse listMedicalRecord(Long accountId, Long petId, ListMedicalRecordRequest request) {
        petAccountService.validateAndGetPetAccount(petId, accountId);

        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;  // 0-based page index
        int perPage = request.getPerPage() > 0 ? request.getPerPage() : 10;

        Pageable pageable = PageRequest.of(page, perPage);
        Page<MedicalRecord> paginatedRecordList = medicalRecordRepository.findByPetIdAndIsDeletedFalseOrderByVisitDateDesc(petId, pageable);

        List<MedicalRecord> medicalRecordList = paginatedRecordList.getContent();

        return ListMedicalRecordResponse.from(
                medicalRecordList,
                paginatedRecordList.getNumber() + 1,
                paginatedRecordList.getTotalPages(),
                paginatedRecordList.getTotalElements()
        );
    }

    private MedicalRecord validateAndGetmedicalRecord(Long petId, Long recordId) {
        MedicalRecord record = medicalRecordRepository.findByIdAndIsDeletedFalse(recordId)
                .orElseThrow(() -> new IllegalArgumentException("진료기록을 찾을 수 없습니다."));
        if (!record.getPetId().equals(petId)) {
            throw new IllegalArgumentException("해당 펫의 기록이 아닙니다.");
        }
        return record;
    }


