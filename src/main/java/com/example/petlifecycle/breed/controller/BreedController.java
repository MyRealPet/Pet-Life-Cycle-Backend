package com.example.petlifecycle.breed.controller;

import com.example.petlifecycle.breed.controller.request.RegisterBreedRequest;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.breed.service.BreedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/petlifecycle/breed")
public class BreedController {

    private final BreedService breedService;

    @PostMapping
    public ResponseEntity<RegisterBreedResponse> register(@RequestBody RegisterBreedRequest request) {
        try {
            log.info("Registering breed: {}", request);
            RegisterBreedResponse response = breedService.registerBreed(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("품종 등록 실패: {}", e.getMessage());
            throw new RuntimeException("품종 등록에 실패했습니다.");
        }
    }

}
