package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.metadata.controller.response.FileUploadResponse;
import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.request.UpdatePetAccountRequest;
import com.example.petlifecycle.pet.controller.response.ListPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.ReadPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.UpdatePetAccountResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PetAccountService {
    RegisterPetAccountResponse registerPetAccount(Long accountId, RegisterPetAccountRequest request);
    ReadPetAccountResponse readPetAccount(Long accountId, Long petId);
    ListPetAccountResponse listPetAccount(Long accountId);
    UpdatePetAccountResponse updatePetAccount (Long accountId, Long petId, UpdatePetAccountRequest request);
    FileUploadResponse uploadProfileImage(Long accountId, Long petId, MultipartFile file);
    FileUploadResponse uploadRegistration(Long accountId, Long petId, MultipartFile file);
}
