package com.example.petlifecycle.vaccinationrecord.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class VaccinationRecordItem {
    private final Long recordId;
    private final LocalDate vaccinationDate;
    private final String hospitalName;
}
