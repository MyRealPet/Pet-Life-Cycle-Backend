package com.example.petlifecycle.mission.pet;

import com.example.petlifecycle.mission.pet.PetDailyMissionDto;
import com.example.petlifecycle.mission.pet.PetDailyMissionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petlifecycle/daily-mission")
@RequiredArgsConstructor
public class PetDailyMissionController {

    private final PetDailyMissionService petDailyMissionService;

    @PostMapping("/{petId}")
    public ResponseEntity<PetDailyMissionDto.DailyMissionResponse> createDailyMission(
            @PathVariable Long petId,
            @RequestBody PetDailyMissionDto.CreateDailyMissionRequest request) {
        PetDailyMissionDto.DailyMissionResponse response = petDailyMissionService.createDailyMission(petId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<List<PetDailyMissionDto.DailyMissionResponse>> getDailyMissions(@PathVariable Long petId) {
        List<PetDailyMissionDto.DailyMissionResponse> response = petDailyMissionService.getDailyMissions(petId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{missionId}/complete")
    public ResponseEntity<Void> completeDailyMission(@PathVariable Long missionId) {
        petDailyMissionService.completeDailyMission(missionId);
        return ResponseEntity.ok().build();
    }
}
