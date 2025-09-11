package com.example.petlifecycle.breed.controller.request;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterBreedRequest {
    private final Species species;
    private final String name;
    private final String description;

    public Breed toBreed() {
        return new Breed(species, name, description);
    }
}
