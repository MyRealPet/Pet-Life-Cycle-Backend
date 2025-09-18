package com.example.petlifecycle.pet.controller.request;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterPetAccountRequest {

    private String name;
    private Long mainBreedId;
    private Long subBreedId;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Boolean isNeutered;
    private Boolean hasMicrochip;
    private MultipartFile profileImg;
    private MultipartFile registerPdf;

    public PetAccount toPetAccount(Long accountId, Breed mainBreed, Breed subBreed) {

        return new PetAccount(
                accountId,
                name,
                mainBreed,
                subBreed,
                gender,
                birthday,
                isNeutered,
                hasMicrochip
        );
    }

    // 파일이 있는지 체크
    public boolean hasProfileImg() {
        return profileImg != null && !profileImg.isEmpty();
    }

    public boolean hasRegisterPdf() {
        return registerPdf != null && !registerPdf.isEmpty();
    }


}
