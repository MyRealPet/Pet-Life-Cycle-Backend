package com.example.petlifecycle.breed.service;

import com.example.petlifecycle.breed.controller.request.RegisterBreedRequest;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;

public interface BreedService {
    RegisterBreedResponse registerBreed(RegisterBreedRequest request);
    ListBreedResponse getAllBreed(ListBreedRequest request);
    ReadBreedResponse readBreed(Long breedId);
    UpdateBreedResponse updateBreed(Long breedId, UpdateBreedRequest request);
    void deleteBreed(Long breedId);
}
