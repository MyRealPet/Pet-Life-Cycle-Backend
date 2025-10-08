package com.example.petlifecycle.dailymission;

import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.repository.PetAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyMissionService {

    private final DailyMissionRepository dailyMissionRepository;
    private final PetAccountRepository petAccountRepository;

    public DailyMissionDto.DailyMissionResponse createDailyMission(Long petId, DailyMissionDto.CreateDailyMissionRequest request) {
        PetAccount petAccount = petAccountRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID: " + petId));

        DailyMission dailyMission = new DailyMission(petAccount, request.getMissionContent());
        dailyMissionRepository.save(dailyMission);

        return DailyMissionDto.DailyMissionResponse.from(dailyMission);
    }

    public List<DailyMissionDto.DailyMissionResponse> getDailyMissions(Long petId) {
        PetAccount petAccount = petAccountRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID: " + petId));

        List<DailyMission> dailyMissions = dailyMissionRepository.findByPetAccountAndDate(petAccount, LocalDate.now());

        return dailyMissions.stream()
                .map(DailyMissionDto.DailyMissionResponse::from)
                .collect(Collectors.toList());
    }

    public void completeDailyMission(Long missionId) {
        DailyMission dailyMission = dailyMissionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid mission ID: " + missionId));

        dailyMission.complete();
        dailyMissionRepository.save(dailyMission);
    }
}
