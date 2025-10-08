package com.example.petlifecycle.health.note.service;

import com.example.petlifecycle.health.note.domain.HealthNote;
import com.example.petlifecycle.health.note.dto.HealthNoteCreateRequest;
import com.example.petlifecycle.health.note.dto.HealthNoteResponse;
import com.example.petlifecycle.health.note.repository.HealthNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthNoteService {

    private final HealthNoteRepository healthNoteRepository;

    @Transactional
    public void saveHealthNote(Long petId, HealthNoteCreateRequest request) {
        LocalDate recordDate = request.getRecordDate() != null ? request.getRecordDate() : LocalDate.now();
        HealthNote healthNote = HealthNote.create(
                petId,
                recordDate,
                request.getMood(),
                request.getPoop(),
                request.getPee(),
                request.getSymptoms()
        );
        healthNoteRepository.save(healthNote);
    }

    public List<HealthNoteResponse> findHealthNotes(Long petId) {
        return healthNoteRepository.findAllByPetIdOrderByRecordDateDesc(petId).stream()
                .map(HealthNoteResponse::from)
                .collect(Collectors.toList());
    }
}
