package com.example.petlifecycle.mission.pet;

import com.example.petlifecycle.pet.entity.PetAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "PetDailyMission")
@Getter
@NoArgsConstructor
public class PetDailyMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetAccount petAccount;

    private String missionContent;

    private boolean isCompleted;

    private LocalDate date;

    public PetDailyMission(PetAccount petAccount, String missionContent) {
        this.petAccount = petAccount;
        this.missionContent = missionContent;
        this.isCompleted = false;
        this.date = LocalDate.now();
    }

    public void complete() {
        this.isCompleted = true;
    }
}
