package com.example.petlifecycle.vaccine.dto;

import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.vaccine.entity.Vaccine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccineRequest {
    private String species;
    private String vaccineName;
    private String description;
    private String sideEffects;
    private Integer vaccineCycle;

    public Vaccine toEntity() {
        return Vaccine.builder()
                .species(Species.valueOf(this.species))
                .vaccineName(this.vaccineName)
                .description(this.description)
                .sideEffects(this.sideEffects)
                .vaccineCycle(this.vaccineCycle)
                .build();
    }
}
