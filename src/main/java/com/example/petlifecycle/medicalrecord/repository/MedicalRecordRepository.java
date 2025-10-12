package com.example.petlifecycle.medicalrecord.repository;

import com.example.petlifecycle.medicalrecord.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Page<MedicalRecord> findByPetIdAndIsDeletedFalseOrderByVisitDateDesc(Long petId, Pageable pageable);
    List<MedicalRecord> findByPetIdAndIsDeletedFalse(Long petId);
    Optional<MedicalRecord> findByIdAndIsDeletedFalse(Long id);
}
