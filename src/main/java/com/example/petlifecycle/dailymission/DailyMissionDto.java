package com.example.petlifecycle.dailymission;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DailyMissionDto {

    @Getter
    @NoArgsConstructor
    public static class CreateDailyMissionRequest {
        private String missionContent;
    }

    @Getter
    public static class DailyMissionResponse {
        private Long id;
        private String missionContent;
        private boolean isCompleted;
        private LocalDate date;

        public DailyMissionResponse(Long id, String missionContent, boolean isCompleted, LocalDate date) {
            this.id = id;
            this.missionContent = missionContent;
            this.isCompleted = isCompleted;
            this.date = date;
        }

        public static DailyMissionResponse from(DailyMission dailyMission) {
            return new DailyMissionResponse(
                    dailyMission.getId(),
                    dailyMission.getMissionContent(),
                    dailyMission.isCompleted(),
                    dailyMission.getDate()
            );
        }
    }
}
