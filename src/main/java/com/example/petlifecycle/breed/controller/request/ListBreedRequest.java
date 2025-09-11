package com.example.petlifecycle.breed.controller.request;

import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListBreedRequest {
    private final int page;
    private final int perPage;
}
