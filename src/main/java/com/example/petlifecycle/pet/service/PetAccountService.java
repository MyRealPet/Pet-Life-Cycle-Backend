package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.response.ListPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.ReadPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;

public interface PetAccountService {
    RegisterPetAccountResponse registerPetAccount(Long accountId, RegisterPetAccountRequest request);
    ReadPetAccountResponse readPetAccount(Long accountId, Long petId);
    ListPetAccountResponse listPetAccount(Long accountId);
}
