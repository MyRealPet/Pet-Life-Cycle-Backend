package com.example.petlifecycle.mission.pet;

import com.example.petlifecycle.pet.entity.PetAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.petlifecycle.mission.pet.PetDailyMission;

import java.time.LocalDate;
import java.util.List;

public interface PetDailyMissionRepository extends JpaRepository<PetDailyMission, Long> {
    List<PetDailyMission> findByPetAccountAndDate(PetAccount petAccount, LocalDate date);
}
