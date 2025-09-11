package com.example.petlifecycle.breed.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterBreedResponse {
    private final String name;
    private final Species species;

    public static RegisterBreedResponse from(Breed breed) {
        return new RegisterBreedResponse(
                breed.getName(),
                breed.getSpecies()
        );
    }
}
