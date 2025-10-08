package com.example.petlifecycle.mission.dto;

import com.example.petlifecycle.mission.domain.DailyMission;
import lombok.Getter;

@Getter
public class DailyMissionResponse {
    private Long id;
    private String name;
    private String description;

    private DailyMissionResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static DailyMissionResponse from(DailyMission dailyMission) {
        return new DailyMissionResponse(
                dailyMission.getId(),
                dailyMission.getName(),
                dailyMission.getDescription()
        );
    }
}
