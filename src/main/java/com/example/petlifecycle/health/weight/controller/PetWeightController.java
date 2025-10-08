package com.example.petlifecycle.health.weight.controller;

import com.example.petlifecycle.health.weight.dto.PetWeightCreateRequest;
import com.example.petlifecycle.health.weight.dto.PetWeightResponse;
import com.example.petlifecycle.health.weight.service.PetWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PetWeightController {

    private final PetWeightService petWeightService;

    @PostMapping("/pets/{petId}/weights")
    public ResponseEntity<Void> createPetWeight(@PathVariable Long petId, @RequestBody PetWeightCreateRequest request) {
        petWeightService.saveWeight(petId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pets/{petId}/weights")
    public ResponseEntity<List<PetWeightResponse>> getPetWeights(@PathVariable Long petId) {
        List<PetWeightResponse> weights = petWeightService.findWeights(petId);
        return ResponseEntity.ok(weights);
    }
}
