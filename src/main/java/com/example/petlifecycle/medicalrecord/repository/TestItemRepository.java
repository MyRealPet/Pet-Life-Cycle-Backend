package com.example.petlifecycle.medicalrecord.repository;

import com.example.petlifecycle.medicalrecord.entity.TestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestItemRepository extends JpaRepository<TestItem, Long> {
    List<TestItem> findByMedicalRecordIdAndIsDeletedFalse(Long recordId);
    Optional<TestItem> findByIdAndIsDeletedFalse(Long id);
}
