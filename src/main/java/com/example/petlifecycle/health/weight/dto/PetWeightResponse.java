package com.example.petlifecycle.health.weight.dto;

import com.example.petlifecycle.health.weight.domain.PetWeight;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PetWeightResponse {
    private Long id;
    private Double weight;
    private LocalDate recordDate;

    private PetWeightResponse(Long id, Double weight, LocalDate recordDate) {
        this.id = id;
        this.weight = weight;
        this.recordDate = recordDate;
    }

    public static PetWeightResponse from(PetWeight petWeight) {
        return new PetWeightResponse(
                petWeight.getId(),
                petWeight.getWeight(),
                petWeight.getRecordDate()
        );
    }
}
