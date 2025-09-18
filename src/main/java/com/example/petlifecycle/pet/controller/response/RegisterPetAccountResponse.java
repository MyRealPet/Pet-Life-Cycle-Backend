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
public class RegisterPetAccountResponse implements FileUrlSetter {
    private final Long petId;
    private final String name;
    private final Long mainBreedId;
    private final Long subBreedId;
    private final String gender;
    private final LocalDate birthday;
    private final Boolean isNeutered;
    private final Boolean hasMicrochip;
    private String profileImgUrl;
    private String registerPdfUrl;
    private final LocalDateTime createdAt;

    public static RegisterPetAccountResponse from(PetAccount petAccount) {
        return RegisterPetAccountResponse.builder()
                .petId(petAccount.getPetId())
                .name(petAccount.getName())
                .mainBreedId(petAccount.getMainBreed().getId())
                // subBreed null 체크
                .subBreedId(Optional.ofNullable(petAccount.getSubBreed())
                        .map(Breed::getId)
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

