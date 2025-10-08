package com.example.petlifecycle.health.note.repository;

import com.example.petlifecycle.health.note.domain.HealthNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthNoteRepository extends JpaRepository<HealthNote, Long> {

    List<HealthNote> findAllByPetIdOrderByRecordDateDesc(Long petId);
}
