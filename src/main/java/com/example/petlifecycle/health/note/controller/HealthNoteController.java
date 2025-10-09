package com.example.petlifecycle.health.note.controller;

import com.example.petlifecycle.health.note.dto.HealthNoteCreateRequest;
import com.example.petlifecycle.health.note.dto.HealthNoteResponse;
import com.example.petlifecycle.health.note.service.HealthNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HealthNoteController {

    private final HealthNoteService healthNoteService;

    @PostMapping("/pets/{petId}/health-notes")
    public ResponseEntity<Void> createHealthNote(@PathVariable Long petId, @RequestBody HealthNoteCreateRequest request) {
        healthNoteService.saveHealthNote(petId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pets/{petId}/health-notes")
    public ResponseEntity<List<HealthNoteResponse>> getHealthNotes(@PathVariable Long petId) {
        List<HealthNoteResponse> notes = healthNoteService.findHealthNotes(petId);
        return ResponseEntity.ok(notes);
    }
}
