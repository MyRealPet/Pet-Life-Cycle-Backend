package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Builder
@RequiredArgsConstructor
public class RegisterPetAccountResponse {
    private final Long petId;
    private final String name;
    private final Long mainBreedId;
    private final Long subBreedId;
    private final String gender;
    private final LocalDate birthday;
    private final Double weight;
    private final Boolean isNeutered;
    private final Boolean hasMicrochip;
    private final MetaDataFile profileImg;
    private final MetaDataFile registerPdf;

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
                .weight(petAccount.getWeight())
                .isNeutered(petAccount.getIsNeutered())
                .hasMicrochip(petAccount.getHasMicrochip())
                // MetaDataFile 전체 객체를 그대로 사용 (null 체크만 추가)
                .profileImg(petAccount.getProfileImg()) // 이미 null일 수 있으므로 그대로
                .registerPdf(petAccount.getRegistrationPdf()) // 이미 null일 수 있으므로 그대로
                .build();
    }

}

