package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.repository.BreedRepository;
import com.example.petlifecycle.metadata.controller.response.FileUploadResponse;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.request.UpdatePetAccountRequest;
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

    @Override
    public ReadPetAccountResponse readPetAccount(Long accountId, Long petId) {

        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);
        ReadPetAccountResponse response = ReadPetAccountResponse.from(petAccount);
        setFileUrl(response, petAccount);

        return response;
    }

    @Override
    public ListPetAccountResponse listPetAccount(Long accountId) {

        try {
            List<PetAccount> petAccounts = petAccountRepository
                    .findByAccountIdAndIsDeletedFalseOrderByCreatedAtDesc(accountId);

            log.info("펫 리스트 조회 완료: accountId={}, count={}", accountId, petAccounts.size());
            return ListPetAccountResponse.from(petAccounts, fileService);
        } catch (Exception e) {
            log.error("펫 리스트 조회 실패");
            throw new RuntimeException("펫 리스트 조회에 실패했습니다.", e);
        }
    }

    @Override
    @Transactional
    public UpdatePetAccountResponse updatePetAccount(Long accountId, Long petId, UpdatePetAccountRequest request) {

        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);
        FileBackup backup = createFileBackup(petAccount);

        petAccount.update(request);

        Long requestedMainBreedId = request.getMainBreedId();
        Long requestedSubBreedId = request.getSubBreedId();
        if (!petAccount.getMainBreed().getId().equals(requestedMainBreedId)) {
            Breed mainBreed = validateAndGetMainBreed(requestedMainBreedId);
            petAccount.setMainBreed(mainBreed);
        }

        Long mainBreedId = petAccount.getMainBreed().getId();
        if (requestedSubBreedId != null) {
            Breed subBreed = validateAndGetSubBreed(requestedSubBreedId, mainBreedId);
            petAccount.setSubBreed(subBreed);
        } else {
            petAccount.setSubBreed(null);
        }

        try {
            handleFileUpdates(petAccount, accountId, request, backup);

        } catch (Exception e) {
            log.error("펫 정보 업데이트 실패: {}", e.getMessage());
            rollbackFile(petAccount, backup);

            throw new RuntimeException("파일 업로드에 실패하여 펫 정보 업데이트를 취소합니다.");
        }

        UpdatePetAccountResponse response = UpdatePetAccountResponse.from(petAccount);
        setFileUrl(response, petAccount);

        return response;
    }

    @Override
    public FileUploadResponse uploadProfileImage(Long accountId, Long petId, MultipartFile file) {
        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);

        MetaDataFile originalProfileImg = petAccount.getProfileImg();
        try {
            if (originalProfileImg != null) {
                fileService.softDeleteFile(originalProfileImg.getId());
            }
            MetaDataFile newProfileImg = fileService.uploadFile(
                    file,
                    FileType.PROFILE_IMAGE,
                    accountId,
                    AccessType.PUBLIC,
                    "PetAccount",
                    petAccount.getPetId()
            );
            petAccount.setProfileImg(newProfileImg);
            petAccountRepository.save(petAccount);

            if (originalProfileImg != null) {
                fileService.hardDeleteFile(originalProfileImg.getId());
            }

            String fileUrl = fileService.getFileUrl(newProfileImg.getS3Key());
            return FileUploadResponse.from(newProfileImg, fileUrl, "프로필 이미지가 성공적으로 업로드되었습니다.");

        } catch (Exception e) {
            log.error("프로필 이미지 업로드 실패, 원본으로 복구: {}", e.getMessage());
            if (originalProfileImg != null) {
                fileService.restoreFile(originalProfileImg.getId());
                petAccount.setProfileImg(originalProfileImg);
                petAccountRepository.save(petAccount);
            }
            throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.");
        }
    }

    @Override
    public FileUploadResponse uploadRegistration(Long accountId, Long petId, MultipartFile file) {
        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);

        MetaDataFile originalRegistrationPdf = petAccount.getRegistrationPdf();
        try {
            if (originalRegistrationPdf != null) {
                fileService.softDeleteFile(originalRegistrationPdf.getId());
            }
            MetaDataFile newRegistrationPdf = fileService.uploadFile(
                    file,
                    FileType.REGISTRATION_PDF,
                    accountId,
                    AccessType.PRIVATE,
                    "PetAccount",
                    petAccount.getPetId()
            );
            petAccount.setRegistrationPdf(newRegistrationPdf);
            petAccountRepository.save(petAccount);

            if (originalRegistrationPdf != null) {
                fileService.hardDeleteFile(originalRegistrationPdf.getId());
            }

            String fileUrl = fileService.getFileUrl(newRegistrationPdf.getS3Key());
            return FileUploadResponse.from(newRegistrationPdf, fileUrl, "동물 등록증이 성공적으로 업로드되었습니다.");
        } catch (Exception e) {
            log.error("동물 등록증 업로드 실패, 원본으로 복구: {}", e.getMessage());
            if (originalRegistrationPdf != null) {
                fileService.restoreFile(originalRegistrationPdf.getId());
                petAccount.setRegistrationPdf(originalRegistrationPdf);
                petAccountRepository.save(petAccount);
            }
            throw new RuntimeException("동물 등록증 업로드에 실패했습니다.");
        }
    }

    @Override
    public void deletePetAccount(Long accountId, Long petId) {
        PetAccount foundPet = getPetAccountWithAccount(petId, accountId);

        foundPet.delete();
        petAccountRepository.save(foundPet);
    }


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

    private static class FileBackup {
        private final MetaDataFile profileImg;
        private final MetaDataFile registrationPdf;

        public FileBackup(MetaDataFile profileImg, MetaDataFile registrationPdf) {
            this.profileImg = profileImg;
            this.registrationPdf = registrationPdf;
        }

        public MetaDataFile getProfileImg() { return profileImg; }
        public MetaDataFile getRegistrationPdf() { return registrationPdf; }
    }

    private FileBackup createFileBackup(PetAccount petAccount) {
        return new FileBackup(
                petAccount.getProfileImg(),
                petAccount.getRegistrationPdf()
        );
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

    private void handleFileUpdates(PetAccount petAccount, Long accountId,
                                   UpdatePetAccountRequest request, FileBackup backup) {

        if (request.hasProfileImg()) {
            if(backup.getProfileImg() != null) {
                fileService.softDeleteFile(backup.getProfileImg().getId());
            }
            MetaDataFile profileImgFile = fileService.uploadFile(
                    request.getProfileImg(),
                    FileType.PROFILE_IMAGE,
                    accountId,
                    AccessType.PUBLIC,
                    "PetAccount",
                    petAccount.getPetId()
            );
            petAccount.setProfileImg(profileImgFile);
            petAccountRepository.save(petAccount);
            if(backup.getProfileImg() != null) {
                fileService.hardDeleteFile(backup.getProfileImg().getId());
            }

        }

        if (request.hasRegisterPdf()) {
            if(backup.getRegistrationPdf() != null) {
                fileService.softDeleteFile(backup.getRegistrationPdf().getId());
            }
            MetaDataFile registrationPdfFile = fileService.uploadFile(
                    request.getRegistrationPdf(),
                    FileType.REGISTRATION_PDF,
                    accountId,
                    AccessType.PRIVATE,
                    "PetAccount",
                    petAccount.getPetId()
            );
            petAccount.setRegistrationPdf(registrationPdfFile);
            petAccountRepository.save(petAccount);
            if(backup.getRegistrationPdf() != null) {
                fileService.hardDeleteFile(backup.getRegistrationPdf().getId());
            }
        }
    }

    public void rollbackFile(PetAccount petAccount, FileBackup backup) {
        try {
            if (backup.getProfileImg() != null) {
                fileService.restoreFile(backup.getProfileImg().getId());
                petAccount.setProfileImg(backup.getProfileImg());
            }
            if (backup.getRegistrationPdf() != null) {
                fileService.restoreFile(backup.getRegistrationPdf().getId());
                petAccount.setRegistrationPdf(backup.getRegistrationPdf());
            }

            petAccountRepository.save(petAccount);
            log.info("파일 롤백 완료: petId={}",petAccount.getPetId());

        } catch (Exception e) {
            log.error("파일 롤백 실패: {}", e.getMessage());
        }
    }
}
