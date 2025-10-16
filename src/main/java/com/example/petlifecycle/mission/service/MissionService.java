package com.example.petlifecycle.mission.service;

import com.example.petlifecycle.mission.domain.DailyMission;
import com.example.petlifecycle.mission.domain.MissionCompletion;
import com.example.petlifecycle.mission.dto.DailyMissionResponse;
import com.example.petlifecycle.mission.dto.MissionCompletionResponse;
import com.example.petlifecycle.mission.dto.MissionStatsResponse;
import com.example.petlifecycle.mission.repository.MasterDailyMissionRepository;
import com.example.petlifecycle.mission.repository.MissionCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MasterDailyMissionRepository masterDailyMissionRepository;
    private final MissionCompletionRepository missionCompletionRepository;

    public List<DailyMissionResponse> getDailyMissions() {
        return masterDailyMissionRepository.findAll().stream()
                .map(DailyMissionResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void completeMission(Long userId, Long missionId) {
        // 이미 완료했는지 확인
        missionCompletionRepository.findByUserIdAndDailyMissionIdAndCompletedDate(userId, missionId, LocalDate.now())
                .ifPresent(completion -> {
                    throw new IllegalStateException("이미 오늘 완료한 미션입니다.");
                });

        DailyMission dailyMission = masterDailyMissionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미션입니다."));

        MissionCompletion completion = MissionCompletion.create(userId, dailyMission);
        missionCompletionRepository.save(completion);
    }

    @Transactional
    public void cancelMissionCompletion(Long userId, Long missionId) {
        MissionCompletion completion = missionCompletionRepository.findByUserIdAndDailyMissionIdAndCompletedDate(userId, missionId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘 완료한 기록이 없는 미션입니다."));

        missionCompletionRepository.delete(completion);
    }

    public List<MissionCompletionResponse> getMissionHistory(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return missionCompletionRepository.findAllByUserIdAndCompletedDateBetween(userId, startDate, endDate).stream()
                .map(MissionCompletionResponse::from)
                .collect(Collectors.toList());
    }

    public MissionStatsResponse getMissionStats(Long userId) {
        long totalCompletions = missionCompletionRepository.countByUserId(userId);
        return new MissionStatsResponse(totalCompletions);
    }
}
