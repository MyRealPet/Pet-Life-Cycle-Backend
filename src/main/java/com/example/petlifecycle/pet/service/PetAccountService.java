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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PetAccountService {

    private final FileService fileService;
    private final PetAccountRepository petAccountRepository;
    private final BreedRepository breedRepository;

    public RegisterPetAccountResponse registerPetAccount(Long accountId, RegisterPetAccountRequest request) {

        validateBreedConstraints(request.getMainBreedId(),request.getCustomMainBreedName(), request.getSubBreedId());

        PetAccount petAccount = request.toPetAccount(accountId);
        PetAccount savedPet = petAccountRepository.save(petAccount);

        handleFileRegistration(savedPet, accountId, request);

        return RegisterPetAccountResponse.from(savedPet.getName());
    }

    public ReadPetAccountResponse readPetAccount(Long accountId, Long petId) {

        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);
        Breed mainBreed = null;
        if (petAccount.getMainBreedId() != null) {
            mainBreed = breedRepository.findByIdAndIsDeletedFalse(petAccount.getMainBreedId())
                    .orElseThrow(() -> new RuntimeException("메인 품종을 찾을 수 없습니다."));
        }
        Breed subBreed = null;
        if (petAccount.getSubBreedId() != null) {
            subBreed = breedRepository.findByIdAndIsDeletedFalse(petAccount.getSubBreedId())
                    .orElseThrow(() -> new RuntimeException("서브 품종을 찾을 수 없습니다."));
        }

        String profileImgUrl = getFileUrlIfAvailable(petAccount.getProfileImg());
        String registrationPdfUrl = getFileUrlIfAvailable(petAccount.getRegistrationPdf());

        return ReadPetAccountResponse.from(petAccount, mainBreed, subBreed, profileImgUrl, registrationPdfUrl);
    }

    public ListPetAccountResponse listPetAccount(Long accountId) {

        try {
            List<PetAccount> petAccounts = petAccountRepository
                    .findByAccountIdAndIsDeletedFalseOrderByCreatedAtDesc(accountId);

            Set<Long> breedIds = petAccounts.stream()
                    .flatMap(pet -> Stream.of(pet.getMainBreedId(), pet.getSubBreedId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<Long, Breed> breedMap = breedRepository.findAllById(breedIds).stream()
                    .collect(Collectors.toMap(Breed::getId, breed -> breed));

            List<ReadPetAccountResponse> petResponses = petAccounts.stream()
                            .map(petAccount -> {
                                Breed mainBreed = breedMap.get(petAccount.getMainBreedId());
                                Breed subBreed = breedMap.get(petAccount.getSubBreedId());
                                String profileImgUrl = getFileUrlIfAvailable(petAccount.getProfileImg());
                                String registrationPdfUrl = getFileUrlIfAvailable(petAccount.getRegistrationPdf());

                                return ReadPetAccountResponse.from(petAccount, mainBreed, subBreed, profileImgUrl, registrationPdfUrl);
                            }).collect(Collectors.toList());

            log.info("펫 리스트 조회 완료: accountId={}, count={}", accountId, petAccounts.size());
            return ListPetAccountResponse.from(petResponses);
        } catch (Exception e) {
            log.error("펫 리스트 조회 실패");
            throw new RuntimeException("펫 리스트 조회에 실패했습니다.", e);
        }
    }

    @Transactional
    public UpdatePetAccountResponse updatePetAccount(Long accountId, Long petId, UpdatePetAccountRequest request) {

        PetAccount petAccount = getPetAccountWithAccount(petId, accountId);
        FileBackup backup = createFileBackup(petAccount);

        validateBreedUpdateConstraints(petAccount, request);

        Long requestedSubBreedId = request.getSubBreedId();
        if (requestedSubBreedId != null) {
            validateSubBreed(requestedSubBreedId, request.getMainBreedId() != null ? request.getMainBreedId() : petAccount.getMainBreedId());
            petAccount.setSubBreedId(requestedSubBreedId);
        } else {
            petAccount.setSubBreedId(null);
        }

        petAccount.update(request);

        try {
            handleFileUpdates(petAccount, accountId, request, backup);

        } catch (Exception e) {
            log.error("펫 정보 업데이트 실패: {}", e.getMessage());
            rollbackFile(petAccount, backup);

            throw new RuntimeException("파일 업로드에 실패하여 펫 정보 업데이트를 취소합니다.");
        }

        return UpdatePetAccountResponse.from(petAccount.getName());
    }

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

    public void deletePetAccount(Long accountId, Long petId) {
        PetAccount foundPet = getPetAccountWithAccount(petId, accountId);

        foundPet.delete();
        petAccountRepository.save(foundPet);
    }


    public PetAccount getPetAccountWithAccount(Long petId, Long accountId) {
        PetAccount petAccount = petAccountRepository.findByPetIdAndIsDeletedFalse(petId)
                .orElseThrow(() -> new RuntimeException("펫 정보를 찾을 수 없습니다."));
        if (!petAccount.getAccountId().equals(accountId)) {
            throw new RuntimeException("해당 펫에 대한 접근 권한이 없습니다.");
        }

        return petAccount;
    }

    private void validateMainBreed(Long mainBreedId) {
        if (!breedRepository.existsByIdAndIsDeletedFalse(mainBreedId)) {
            throw new RuntimeException("메인 품종을 찾을 수 없습니다.");
        }
    }

    private void validateSubBreed(Long subBreedId, Long mainBreedId) {
        if (mainBreedId == null) {
            throw new RuntimeException("메인 품종이 없으면 서브 품종을 입력할 수 없습니다.");
        }
        if (subBreedId.equals(mainBreedId)) {
            throw new RuntimeException("메인 품종과 같은 서브 품종을 입력할 수 없습니다.");
        }
        Breed mainBreed = breedRepository.findByIdAndIsDeletedFalse(mainBreedId)
                .orElseThrow(() -> new RuntimeException("서브 품종을 찾을 수 없습니다."));
        Breed subBreed = breedRepository.findByIdAndIsDeletedFalse(subBreedId)
                .orElseThrow(() -> new RuntimeException("서브 품종을 찾을 수 없습니다."));
        if (!subBreed.getSpecies().equals(mainBreed.getSpecies())) {
            throw new RuntimeException("메인 종과 다른 서브 종을 입력할 수 없습니다.");
        }

    }

    private void validateBreedConstraints(Long mainBreedId, String customMainBreedName, Long subBreedId) {
        boolean hasMain = (mainBreedId != null);
        boolean hasCustom = (customMainBreedName != null && !customMainBreedName.trim().isEmpty());

        if (!hasMain && !hasCustom) {
            throw new RuntimeException("품종 정보를 입력해주세요.");
        }
        if (hasMain && hasCustom) {
            throw new RuntimeException("메인 품종과 커스텀 품종은 동시에 설정할 수 없습니다.");
        }

        if (hasMain) validateMainBreed(mainBreedId);
        if (subBreedId != null && hasMain) validateSubBreed(subBreedId, mainBreedId);
    }

    private void validateBreedUpdateConstraints(PetAccount petAccount, UpdatePetAccountRequest request) {
        Long currentMain = petAccount.getMainBreedId();
        Long requestedMain = request.getMainBreedId();
        String requestedCustom = request.getCustomMainBreedName();
        Boolean hasRequestedCustom = requestedCustom != null && !requestedCustom.trim().isEmpty();

        if (currentMain == null) {
            if ((requestedMain == null) && !hasRequestedCustom) {
                throw new RuntimeException("품종 정보를 입력해주세요.");
            }
            if (requestedMain != null) {
                validateMainBreed(requestedMain);
                petAccount.setMainBreedId(requestedMain);
                petAccount.setCustomMainBreedName(null);
            } else if (hasRequestedCustom) {
                petAccount.setCustomMainBreedName(requestedCustom.trim());
            }
        } else {
            if (requestedMain != null) {
                if (hasRequestedCustom) {
                    throw new RuntimeException("커스텀 품종은 메인 품종과 동시에 설정할 수 없습니다.");
                }
                if (!currentMain.equals(requestedMain)) {
                    validateMainBreed(requestedMain);
                    petAccount.setMainBreedId(requestedMain);
                }
                petAccount.setCustomMainBreedName(null);
            } else if (hasRequestedCustom) {
                petAccount.setMainBreedId(null);
                petAccount.setCustomMainBreedName(requestedCustom.trim());
            }
        }
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

    private String getFileUrlIfAvailable(MetaDataFile file) {
        return (file != null && file.isAvailable()) ? fileService.getFileUrl(file.getS3Key()) : null;
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
                        request.getRegistrationPdf(),
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

        if (Boolean.TRUE.equals(request.getDeleteProfileImg())) {
            if (backup.getProfileImg() != null) {
                fileService.softDeleteFile(backup.getProfileImg().getId());
            }
            petAccount.setProfileImg(null);
            petAccountRepository.save(petAccount);
        } else if (request.hasProfileImg()) {
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

        if (Boolean.TRUE.equals(request.getDeleteRegistrationPdf())) {
            if (backup.getRegistrationPdf() != null) {
                fileService.softDeleteFile(backup.getRegistrationPdf().getId());
            }
            petAccount.setRegistrationPdf(null);
            petAccountRepository.save(petAccount);
        } else if (request.hasRegisterPdf()) {
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
