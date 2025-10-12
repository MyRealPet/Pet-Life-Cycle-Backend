package com.example.petlifecycle.medicalrecord.controller.response;

import com.example.petlifecycle.medicalrecord.controller.dto.FileInfoDto;
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
public class ReadMedicalRecordResponse {
    private final Long id;
    private final Long petId;

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

    // 청구서 파일
    private final FileInfoDto receiptFile;

    // 첨부 파일
    private final List<FileInfoDto> attachmentFiles;

    // 상세 항목들
    private final List<TestItemDto> testItems;
    private final List<TreatmentItemDto> treatmentItems;
    private final List<MedicationItemDto> medicationItems;
}
