package com.example.petlifecycle.medicalrecord.repository;

import com.example.petlifecycle.medicalrecord.entity.TreatmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreatmentItemRepository extends JpaRepository<TreatmentItem, Long> {
    List<TreatmentItem> findByMedicalRecordIdAndIsDeletedFalse(Long recordId);
    Optional<TreatmentItem> findByIdAndIsDeletedFalse(Long id);
}
