package com.example.petlifecycle.pet.controller;

import com.example.petlifecycle.auth.service.AuthService;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.request.UpdatePetAccountRequest;
import com.example.petlifecycle.pet.controller.response.ListPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.ReadPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.UpdatePetAccountResponse;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.service.PetAccountService;
import com.example.petlifecycle.redis_cache.RedisCacheService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/petlifecycle/profile")
public class PetAccountController {

    private final PetAccountService petAccountService;
    private final AuthService authService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterPetAccountResponse> register(@RequestHeader("Authorization") String authorizedHeader, @ModelAttribute RegisterPetAccountRequest request) {

        Long accountId = authService.getAccountIdFromToken(authorizedHeader);

        try {
            log.info("Registering pet account: {}", request);
            RegisterPetAccountResponse response = petAccountService.registerPetAccount(accountId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("펫 등록 실패: {}", e.getMessage());
            throw new RuntimeException("펫 등록에 실패했습니다.");
        }
    }

    @GetMapping
    public ResponseEntity<ListPetAccountResponse> list(@RequestHeader("Authorization") String authorizedHeader) {

        Long accountId = authService.getAccountIdFromToken(authorizedHeader);

        try {
            log.info("Reading pet account: {}", accountId);
            ListPetAccountResponse response = petAccountService.listPetAccount(accountId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("펫 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("펫 정보를 불러오는데 실패했습니다.");
        }
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ReadPetAccountResponse> read(@RequestHeader("Authorization")String authorizedHeader, @PathVariable Long petId) {

        Long accountId = authService.getAccountIdFromToken(authorizedHeader);

        try {
            log.info("Reading pet account: {}", petId);
            ReadPetAccountResponse response = petAccountService.readPetAccount(accountId, petId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("펫 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("펫 정보를 불러오는데 실패했습니다.");
        }
    }


    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdatePetAccountResponse> update(@RequestHeader("Authorization")String authorizedHeader, @PathVariable("petId") Long petId, @ModelAttribute UpdatePetAccountRequest request) {

        Long accountId = authService.getAccountIdFromToken(authorizedHeader);

        try {
            log.info("Registering pet account: {}", request);
            UpdatePetAccountResponse response = petAccountService.updatePetAccount(accountId, petId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("펫 수정 실패: {}", e.getMessage());
            throw new RuntimeException("펫 정보 수정에 실패했습니다.");
        }
    }
}
