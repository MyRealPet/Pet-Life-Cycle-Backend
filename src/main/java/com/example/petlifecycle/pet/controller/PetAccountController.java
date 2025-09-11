package com.example.petlifecycle.pet.controller;

import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.service.PetAccountService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterPetAccountResponse> register(@ModelAttribute RegisterPetAccountRequest request) {
        try {
            log.info("Registering pet account: {}", request);
            RegisterPetAccountResponse response = petAccountService.registerPetAccount(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("펫 등록 실패: {}", e.getMessage());
            throw new RuntimeException("펫 등록에 실패했습니다.");
        }
    }

}
