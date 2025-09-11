package com.example.petlifecycle.breed.controller.response;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReadBreedResponse {
    private final Long breedId;
    private final String name;
    private final Species species;
    private final String description;

    public static ReadBreedResponse from(Breed breed) {
        return new ReadBreedResponse(
                breed.getId(),
                breed.getName(),
                breed.getSpecies(),
                breed.getDescription()
        );
    }
}
