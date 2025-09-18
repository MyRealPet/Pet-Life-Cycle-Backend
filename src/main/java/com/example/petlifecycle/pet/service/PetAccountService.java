package com.example.petlifecycle.pet.service;

import com.example.petlifecycle.pet.controller.request.RegisterPetAccountRequest;
import com.example.petlifecycle.pet.controller.response.ListPetAccountResponse;
import com.example.petlifecycle.pet.controller.response.RegisterPetAccountResponse;

public interface PetAccountService {
    RegisterPetAccountResponse registerPetAccount(RegisterPetAccountRequest request);
    ListPetAccountResponse listPetAccount(Long accountId);
}
