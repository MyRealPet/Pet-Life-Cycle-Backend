package com.example.petlifecycle.cycle.controller;

import com.example.petlifecycle.cycle.dto.PetCycleCreateRequest;
import com.example.petlifecycle.cycle.dto.PetCycleResponse;
import com.example.petlifecycle.cycle.service.PetCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PetCycleController {

    private final PetCycleService petCycleService;

    @PostMapping("/pets/{petId}/cycles")
    public ResponseEntity<Void> createPetCycle(@PathVariable Long petId, @RequestBody PetCycleCreateRequest request) {
        petCycleService.saveCycle(petId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pets/{petId}/cycles")
    public ResponseEntity<List<PetCycleResponse>> getPetCycles(@PathVariable Long petId) {
        List<PetCycleResponse> cycles = petCycleService.findCycles(petId);
        return ResponseEntity.ok(cycles);
    }

    @DeleteMapping("/pets/{petId}/cycles/{cycleId}")
    public ResponseEntity<Void> deletePetCycle(@PathVariable Long petId, @PathVariable Long cycleId) {
        // petId는 인가(Authorization)에 사용할 수 있으나, 현재 로직에서는 cycleId만으로 삭제 처리
        petCycleService.deleteCycle(cycleId);
        return ResponseEntity.noContent().build();
    }
}
