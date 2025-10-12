package com.example.petlifecycle.medicalrecord.repository;

import com.example.petlifecycle.medicalrecord.entity.MedicationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MedicationItemRepository extends JpaRepository<MedicationItem, Long> {
    List<MedicationItem> findByMedicalRecordIdAndIsDeletedFalse(Long recordId);
    Optional<MedicationItem> findByIdAndIsDeletedFalse(Long id);
}
