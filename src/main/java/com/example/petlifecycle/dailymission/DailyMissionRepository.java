package com.example.petlifecycle.dailymission;

import com.example.petlifecycle.pet.entity.PetAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyMissionRepository extends JpaRepository<DailyMission, Long> {
    List<DailyMission> findByPetAccountAndDate(PetAccount petAccount, LocalDate date);
}
