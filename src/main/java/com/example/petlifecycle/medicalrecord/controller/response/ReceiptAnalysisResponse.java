package com.example.petlifecycle.medicalrecord.controller.response;

import com.example.petlifecycle.medicalrecord.controller.dto.MedicationItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TestItemDto;
import com.example.petlifecycle.medicalrecord.controller.dto.TreatmentItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptAnalysisResponse {
    @JsonProperty("hospitalName")
    private String hospitalName;

    @JsonProperty("hospitalNumber")
    private String hospitalNumber;

    @JsonProperty("hospitalAddress")
    private String hospitalAddress;

    @JsonProperty("visitDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate;

    @JsonProperty("totalAmount")
    private Integer totalAmount;

    @JsonProperty("vatAmount")
    private Integer vatAmount;

    @JsonProperty("testItems")
    private List<TestItemDto> testItems = new ArrayList<>();

    @JsonProperty("treatmentItems")
    private List<TreatmentItemDto> treatmentItems = new ArrayList<>();

    @JsonProperty("medicationItems")
    private List<MedicationItemDto> medicationItems = new ArrayList<>();

    @JsonProperty("message")
    private String message;
}


