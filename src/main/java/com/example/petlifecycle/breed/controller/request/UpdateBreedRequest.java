package com.example.petlifecycle.breed.controller.request;

import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateBreedRequest {
    private final String name;
    private final Species species;
    private final String description;
}
