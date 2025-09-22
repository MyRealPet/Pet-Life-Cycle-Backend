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
    private String customMainBreedName;
    private Long subBreedId;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Boolean isNeutered;
    private Boolean hasMicrochip;
    private Long registrationNum;
    private MultipartFile profileImg;
    private MultipartFile registerPdf;

    public PetAccount toPetAccount(Long accountId) {

        return new PetAccount(
                accountId,
                name,
                mainBreedId,
                customMainBreedName,
                subBreedId,
                gender,
                birthday,
                isNeutered,
                hasMicrochip,
                registrationNum
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
