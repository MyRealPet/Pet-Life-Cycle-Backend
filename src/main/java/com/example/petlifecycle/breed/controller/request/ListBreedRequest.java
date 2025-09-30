package com.example.petlifecycle.breed.controller.request;

import com.example.petlifecycle.breed.entity.Species;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListBreedRequest {
    private int page = 0;
    private int perPage = 100;
}
