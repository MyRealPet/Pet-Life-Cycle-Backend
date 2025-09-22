package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class UpdatePetAccountResponse {
    private final String message;

    public static UpdatePetAccountResponse from(String petName) {
        String message = "\"" + petName + "\"" + " 정보가 성공적으로 수정되었습니다.";
        return UpdatePetAccountResponse.builder()
                .message(message)
                .build();
    }
}
