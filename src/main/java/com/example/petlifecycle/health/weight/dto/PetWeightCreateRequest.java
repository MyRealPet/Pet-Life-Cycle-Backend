package com.example.petlifecycle.health.weight.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PetWeightCreateRequest {
    private Double weight;
    private LocalDate recordDate;
}
