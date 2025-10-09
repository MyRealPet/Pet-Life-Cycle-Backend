package com.example.petlifecycle.breed.controller;

import com.example.petlifecycle.breed.controller.request.ListBreedRequest;
import com.example.petlifecycle.breed.controller.request.RegisterBreedRequest;
import com.example.petlifecycle.breed.controller.request.UpdateBreedRequest;
import com.example.petlifecycle.breed.controller.response.ListBreedResponse;
import com.example.petlifecycle.breed.controller.response.ReadBreedResponse;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.breed.controller.response.UpdateBreedResponse;
import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.breed.service.BreedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/pet/breed")
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

    @GetMapping
    public ResponseEntity<ListBreedResponse> list(@ModelAttribute ListBreedRequest request) {
        try {
            ListBreedResponse response = breedService.getAllBreed(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("품종 목록 조회 실패: {}", e.getMessage(), e);
            throw new RuntimeException("품종 목록 조회에 실패했습니다.");
        }
    }

    @GetMapping("/{breedId}")
    public ResponseEntity<ReadBreedResponse> read(@PathVariable Long breedId) {
        try {
            ReadBreedResponse response = breedService.readBreed(breedId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("품종 조회 실패 (ID: {}): {}", breedId, e.getMessage(), e);
            throw new RuntimeException("품종 조회에 실패했습니다.");
        }
    }

    @PutMapping("/{breedId}")
    public ResponseEntity<UpdateBreedResponse> update(@PathVariable Long breedId, @RequestBody UpdateBreedRequest request) {
        try {
            UpdateBreedResponse response = breedService.updateBreed(breedId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("품종 수정 실패 (ID: {}): {}", breedId, e.getMessage(), e);
            throw new RuntimeException("품종 수정에 실패했습니다.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@PathVariable Long breedId) {
        try {
            breedService.deleteBreed(breedId);
            return ResponseEntity.ok("파일 삭제에 성공했습니다.");
        } catch (Exception e) {
            log.error("품종 삭제 실패 (ID: {}): {}", breedId, e.getMessage(), e);
            throw new RuntimeException("파일 삭제에 실패했습니다.");
        }
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ListBreedResponse> dropdown(@RequestParam Species species, @ModelAttribute ListBreedRequest request) {
        try {
            ListBreedResponse response = breedService.getBreedBySpecies(species, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("품종 목록 조회 실패: {}", e.getMessage(), e);
            throw new RuntimeException("품종 목록 조회에 실패했습니다.");
        }
    }
}
