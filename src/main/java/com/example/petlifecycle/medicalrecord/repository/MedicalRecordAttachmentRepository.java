package com.example.petlifecycle.medicalrecord.repository;

import com.example.petlifecycle.medicalrecord.entity.MedicalRecordAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordAttachmentRepository extends JpaRepository<MedicalRecordAttachment, Long> {
    List<MedicalRecordAttachment> findByMedicalRecordIdAndIsDeletedFalse(Long recordId);
    Optional<MedicalRecordAttachment> findByIdAndIsDeletedFalse(Long id);
}
