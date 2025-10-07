package com.example.petlifecycle.vaccinationrecord.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class VaccineWithRecordsDto {
    private final Long vaccineId;
    private final String vaccineName;
    private final String description;
    private final List<VaccinationRecordDto> vaccinationRecords;
}
