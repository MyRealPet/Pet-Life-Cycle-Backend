package com.example.petlifecycle.dailymission;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petlifecycle/daily-mission")
@RequiredArgsConstructor
public class DailyMissionController {

    private final DailyMissionService dailyMissionService;

    @PostMapping("/{petId}")
    public ResponseEntity<DailyMissionDto.DailyMissionResponse> createDailyMission(
            @PathVariable Long petId,
            @RequestBody DailyMissionDto.CreateDailyMissionRequest request) {
        DailyMissionDto.DailyMissionResponse response = dailyMissionService.createDailyMission(petId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<List<DailyMissionDto.DailyMissionResponse>> getDailyMissions(@PathVariable Long petId) {
        List<DailyMissionDto.DailyMissionResponse> response = dailyMissionService.getDailyMissions(petId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{missionId}/complete")
    public ResponseEntity<Void> completeDailyMission(@PathVariable Long missionId) {
        dailyMissionService.completeDailyMission(missionId);
        return ResponseEntity.ok().build();
    }
}
