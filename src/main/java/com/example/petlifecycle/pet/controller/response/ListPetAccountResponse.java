package com.example.petlifecycle.pet.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListPetAccountResponse {
    private final List<ReadPetAccountResponse> pets;

    public static ListPetAccountResponse from(List<ReadPetAccountResponse> petResponses) {
        return new ListPetAccountResponse(petResponses);
    }
}
