package com.example.petlifecycle.pet.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.entity.PetAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ListPetAccountResponse {
    private final List<PetAccountSummary> pets;

    public static ListPetAccountResponse from(List<PetAccount> petAccounts, FileService fileService) {
        List<PetAccountSummary> pets = petAccounts.stream()
                .map(pet -> PetAccountSummary.from(pet, fileService))
                .collect(Collectors.toList());

        return new ListPetAccountResponse(pets);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PetAccountSummary {
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

        public static PetAccountSummary from(PetAccount petAccount, FileService fileService) {
            String profileImgUrl;
            if (petAccount.getProfileImg() != null && petAccount.getProfileImg().isAvailable()) {
                profileImgUrl = fileService.getFileUrl(petAccount.getProfileImg().getS3Key());
            }

            return PetAccountSummary.builder()
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
                    .build();
        }
    }
}
