package com.example.petlifecycle.vaccinationrecord.repository;

import com.example.petlifecycle.vaccinationrecord.entity.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    Optional<VaccinationRecord> findByIdAndIsDeletedFalse(Long id);
    List<VaccinationRecord> findByPetIdAndIsDeletedFalseOrderByVaccinationDateDesc(Long petId);
}
