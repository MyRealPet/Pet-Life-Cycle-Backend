package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.repository.BreedRepository;
import com.example.petlifecycle.metadata.controller.response.FileUploadResponse;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.response.*;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.repository.PetAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PetAccountServiceImpl implements PetAccountService {

    private final FileService fileService;
    private final PetAccountRepository petAccountRepository;
    private final BreedRepository breedRepository;

    @Override
    public RegisterPetAccountResponse registerPetAccount(Long accountId, RegisterPetAccountRequest request) {

        Breed mainBreed = validateAndGetMainBreed(request.getMainBreedId());

        Breed subBreed = null;
        if (request.getSubBreedId() != null) {
            subBreed = validateAndGetSubBreed(request.getSubBreedId(), request.getMainBreedId());
        }

        PetAccount petAccount = request.toPetAccount(accountId, mainBreed, subBreed);
        PetAccount savedPet = petAccountRepository.save(petAccount);

        handleFileRegistration(savedPet, accountId, request);

        RegisterPetAccountResponse response = RegisterPetAccountResponse.from(savedPet);
        setFileUrl(response, savedPet);

        return response;
    }


            if (subBreed.equals(mainBreed)) {
                throw new RuntimeException("메인 품종과 같은 서브 품종을 입력할 수 없습니다.");
            }
        }

        PetAccount petAccount = request.toPetAccount(mainBreed, subBreed);
        PetAccount savedPet = petAccountRepository.save(petAccount);

        try {
            // 프로필 이미지 업로드 (있는 경우)
            if (request.hasProfileImg()) {


    private PetAccount getPetAccountWithAccount(Long petId, Long ownerId) {
        PetAccount petAccount = petAccountRepository.findByPetIdAndIsDeletedFalse(petId)
                .orElseThrow(() -> new RuntimeException("펫 정보를 찾을 수 없습니다."));
        if (!petAccount.getAccountId().equals(ownerId)) {
            throw new RuntimeException("해당 펫에 대한 접근 권한이 없습니다.");
        }

        return petAccount;
    }

    private Breed validateAndGetMainBreed(Long mainBreedId) {
        Breed mainBreed = breedRepository.findByIdAndIsDeletedFalse(mainBreedId)
                .orElseThrow(() -> new RuntimeException("메인 품종을 찾을 수 없습니다."));

        return mainBreed;
    }

    private Breed validateAndGetSubBreed(Long subBreedId, Long mainBreedId) {
        if (subBreedId.equals(mainBreedId)) {
            throw new RuntimeException("메인 품종과 같은 서브 품종을 입력할 수 없습니다.");
        }
        Breed subBreed = breedRepository.findByIdAndIsDeletedFalse(subBreedId)
                .orElseThrow(() -> new RuntimeException("서브 품종을 찾을 수 없습니다."));

        return subBreed;
    }

    private void setFileUrl(FileUrlSetter response, PetAccount petAccount) {
        if (petAccount.getProfileImg() != null) {
            String profileImgUrl = fileService.getFileUrl(petAccount.getProfileImg().getS3Key());
            response.setProfileImgUrl(profileImgUrl);
        }
        if (petAccount.getRegistrationPdf() != null) {
            String registrationPdf = fileService.getFileUrl(petAccount.getRegistrationPdf().getS3Key());
            response.setRegisterPdfUrl(registrationPdf);
        }
    }

    private void handleFileRegistration(PetAccount petAccount, Long accountId, RegisterPetAccountRequest request) {
        try {
            if (request.hasProfileImg()) {
                MetaDataFile profileImg = fileService.uploadFile(
                        request.getProfileImg(),
                        FileType.PROFILE_IMAGE,
                        accountId,
                        AccessType.PUBLIC,
                        "PetAccount",
                        petAccount.getPetId()
                );
                petAccount.setProfileImg(profileImg);
            }

            if (request.hasRegisterPdf()) {
                MetaDataFile registerPdf = fileService.uploadFile(
                        request.getRegisterPdf(),
                        FileType.REGISTRATION_PDF,
                        accountId,
                        AccessType.PRIVATE,
                        "PetAccount",
                        petAccount.getPetId()
                );
                petAccount.setRegistrationPdf(registerPdf);
            }
            petAccountRepository.save(petAccount);

        } catch (Exception e) {
            log.error("파일 업로드 실패로 인한 반려동물 등록 취소: {}", e.getMessage());
            throw new RuntimeException("파일 업로드에 실패하여 펫 계정 생성을 취소합니다.", e);
        }
    }
        }
    }
}
