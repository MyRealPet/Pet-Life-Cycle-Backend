package com.example.petlifecycle.vaccinationrecord.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListVacRecordResponse {

    private final List<VaccineWithRecordDto> vacRecords;

}

