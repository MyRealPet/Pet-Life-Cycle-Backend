package com.example.petlifecycle.medicalrecord.controller.request;

import com.example.petlifecycle.medicalrecord.controller.dto.MedicationItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TestItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TreatmentItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UpdateMedicalRecordRequest {
    // 행정정보
    private final String hospitalName;
    private final String hospitalNumber;
    private final String hospitalAddress;
    private final LocalDate visitDate;
    private final Integer totalAmount;
    private final Integer vatAmount;

    // 진단 및 증상
    private final String diagnosis;
    private final String symptoms;

    // 청구서 파일 ID
    private final Long receiptFileId;

    // 첨부 파일 IDs
    private final List<Long> attachmentFileIds;

    // 상세 항목들
    private final List<TestItemDto> testItems;
    private final List<TreatmentItemDto> treatmentItems;
    private final List<MedicationItemDto> medicationItems;
}
