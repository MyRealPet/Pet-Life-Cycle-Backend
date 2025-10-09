package com.example.petlifecycle.mission.pet;

import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.repository.PetAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.petlifecycle.mission.pet.PetDailyMission;
import com.example.petlifecycle.mission.pet.PetDailyMissionDto;
import com.example.petlifecycle.mission.pet.PetDailyMissionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetDailyMissionService {

    private final PetDailyMissionRepository petDailyMissionRepository;
    private final PetAccountRepository petAccountRepository;

    public PetDailyMissionDto.DailyMissionResponse createDailyMission(Long petId, PetDailyMissionDto.CreateDailyMissionRequest request) {
        PetAccount petAccount = petAccountRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID: " + petId));

        PetDailyMission petDailyMission = new PetDailyMission(petAccount, request.getMissionContent());
        petDailyMissionRepository.save(petDailyMission);

        return PetDailyMissionDto.DailyMissionResponse.from(petDailyMission);
    }

    public List<PetDailyMissionDto.DailyMissionResponse> getDailyMissions(Long petId) {
        PetAccount petAccount = petAccountRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID: " + petId));

        List<PetDailyMission> petDailyMissions = petDailyMissionRepository.findByPetAccountAndDate(petAccount, LocalDate.now());

        return petDailyMissions.stream()
                .map(PetDailyMissionDto.DailyMissionResponse::from)
                .collect(Collectors.toList());
    }

    public void completeDailyMission(Long missionId) {
        PetDailyMission petDailyMission = petDailyMissionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid mission ID: " + missionId));

        petDailyMission.complete();
        petDailyMissionRepository.save(petDailyMission);
    }
}
