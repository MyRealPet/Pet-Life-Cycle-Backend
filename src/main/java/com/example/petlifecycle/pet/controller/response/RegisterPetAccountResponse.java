package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class RegisterPetAccountResponse {
    private final String message;

    public static RegisterPetAccountResponse from(String petName) {
        String message = "\"" + petName + "\"" + "이(가) 성공적으로 등록되었습니다!";
        return RegisterPetAccountResponse.builder()
                .message(message)
                .build();
    }


}

