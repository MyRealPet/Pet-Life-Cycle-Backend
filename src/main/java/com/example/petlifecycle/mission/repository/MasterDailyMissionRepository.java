package com.example.petlifecycle.mission.repository;

import com.example.petlifecycle.mission.domain.DailyMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterDailyMissionRepository extends JpaRepository<DailyMission, Long> {
}
