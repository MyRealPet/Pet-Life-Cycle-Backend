package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReadPetAccountResponse {
    private final Long petId;
    private final String name;
    private final Species species;
    private final Long mainBreedId;
    private final String mainBreedName;
    private final String customMainBreedName;
    private final Long subBreedId;
    private final String subBreedName;
    private final String gender;
    private final LocalDate birthday;
    private final Boolean isNeutered;
    private final Boolean hasMicrochip;
    private final Long registrationNum;
    private String profileImgUrl;
    private String registerPdfUrl;
    private final LocalDateTime createdAt;

    public static ReadPetAccountResponse from(PetAccount petAccount, Breed mainBreed, Breed subBreed, FileService fileService) {
        String profileImgUrl = null;
        if (petAccount.getProfileImg() != null && petAccount.getProfileImg().isAvailable()) {
            profileImgUrl = fileService.getFileUrl(petAccount.getProfileImg().getS3Key());
        }

        String registerPdfUrl = null;
        if (petAccount.getRegistrationPdf() != null && petAccount.getRegistrationPdf().isAvailable()) {
            registerPdfUrl = fileService.getFileUrl(petAccount.getRegistrationPdf().getS3Key());
        }

        return ReadPetAccountResponse.builder()
                .petId(petAccount.getPetId())
                .name(petAccount.getName())
                .species(mainBreed.getSpecies())
                .mainBreedId(mainBreed.getId())
                .mainBreedName(mainBreed.getName())
                .customMainBreedName(petAccount.getCustomMainBreedName())
                .subBreedId(subBreed != null ? subBreed.getId() : null)
                .subBreedName(subBreed != null ? subBreed.getName() : null)
                .gender(petAccount.getGender())
                .birthday(petAccount.getBirthday())
                .isNeutered(petAccount.getIsNeutered())
                .hasMicrochip(petAccount.getHasMicrochip())
                .registrationNum(petAccount.getRegistrationNum())
                .profileImgUrl(profileImgUrl)
                .registerPdfUrl(registerPdfUrl)
                .createdAt(petAccount.getCreatedAt()) // LocalDateTime.now() -> petAccount.getCreatedAt()
                .build();
    }

}
