package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class ReadPetAccountResponse implements FileUrlSetter {
    private final Long petId;
    private final String name;
    private final Species species;
    private final Long mainBreedId;
    private final String mainBreedName;
    private final Long subBreedId;
    private final String subBreedName;
    private final String gender;
    private final LocalDate birthday;
    private final Boolean isNeutered;
    private final Boolean hasMicrochip;
    private String profileImgUrl;
    private String registerPdfUrl;
    private final LocalDateTime createdAt;

    public static ReadPetAccountResponse from(PetAccount petAccount) {
        return ReadPetAccountResponse.builder()
                .petId(petAccount.getPetId())
                .name(petAccount.getName())
                .species(petAccount.getMainBreed().getSpecies())
                .mainBreedId(petAccount.getMainBreed().getId())
                .mainBreedName(petAccount.getMainBreed().getName())
                .subBreedId(Optional.ofNullable(petAccount.getSubBreed())
                        .map(Breed::getId)
                        .orElse(null))
                .subBreedName(Optional.ofNullable(petAccount.getSubBreed())
                        .map(Breed::getName)
                        .orElse(null))
                .gender(petAccount.getGender())
                .birthday(petAccount.getBirthday())
                .isNeutered(petAccount.getIsNeutered())
                .hasMicrochip(petAccount.getHasMicrochip())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    @Override
    public void setRegisterPdfUrl(String registerPdfUrl) {
        this.registerPdfUrl = registerPdfUrl;
    }
}
