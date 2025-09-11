package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.repository.BreedRepository;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.repository.PetAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PetAccountServiceImpl implements PetAccountService {

    private final FileService fileService;
    private final PetAccountRepository petAccountRepository;
    private final BreedRepository breedRepository;

    @Override
    public RegisterPetAccountResponse registerPetAccount(RegisterPetAccountRequest request) {

        Breed mainBreed =  breedRepository.findById(request.getMainBreedId())
                .orElseThrow(() -> new RuntimeException("메인 품종을 찾을 수 없습니다."));

        Breed subBreed = null;
        if (request.getSubBreedId() != null) {
            subBreed = breedRepository.findById(request.getSubBreedId())
                    .orElseThrow(() -> new RuntimeException("서브 품종을 찾을 수 없습니다."));

            if (subBreed.equals(mainBreed)) {
                throw new RuntimeException("메인 품종과 같은 서브 품종을 입력할 수 없습니다.");
            }
        }

        PetAccount petAccount = request.toPetAccount(mainBreed, subBreed);
        PetAccount savedPet = petAccountRepository.save(petAccount);

        try {
            // 프로필 이미지 업로드 (있는 경우)
            if (request.hasProfileImg()) {

                MetaDataFile profileImg = fileService.uploadFile(
                        request.getProfileImg(),
                        FileType.PROFILE_IMAGE,
                        AccessType.PUBLIC,  // 프로필 이미지 공개
                        "PetAccount",
                        savedPet.getPetId()
                );
                savedPet.setProfileImg(profileImg);
            }

            // 등록증 PDF 업로드 (있는 경우)
            if (request.hasRegisterPdf()) {
                MetaDataFile registerPdf = fileService.uploadFile(
                        request.getRegisterPdf(),
                        FileType.REGISTRATION_PDF,
                        AccessType.PRIVATE,  // 등록증은 비공개
                        "PetAccount",
                        savedPet.getPetId()
                );
                savedPet.setRegistrationPdf(registerPdf);
            }

            petAccountRepository.save(savedPet);
            RegisterPetAccountResponse response = RegisterPetAccountResponse.from(savedPet);
            return response;

        } catch (Exception e) {
            // 파일 업로드 실패시 생성된 PetAccount 삭제
            log.error("파일 업로드 실패로 인한 반려동물 등록 취소: {}", e.getMessage());
            savedPet.delete();
            petAccountRepository.save(savedPet);
            throw new RuntimeException("반려동물 등록 중 파일 업로드에 실패했습니다.", e);
        }
    }
}
