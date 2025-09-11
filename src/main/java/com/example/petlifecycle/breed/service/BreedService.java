package com.example.petlifecycle.breed.service;

import com.example.petlifecycle.breed.controller.request.ListBreedRequest;
import com.example.petlifecycle.breed.controller.request.RegisterBreedRequest;
import com.example.petlifecycle.breed.controller.request.UpdateBreedRequest;
import com.example.petlifecycle.breed.controller.response.ListBreedResponse;
import com.example.petlifecycle.breed.controller.response.ReadBreedResponse;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.breed.controller.response.UpdateBreedResponse;

public interface BreedService {
    RegisterBreedResponse registerBreed(RegisterBreedRequest request);
    ListBreedResponse getAllBreed(ListBreedRequest request);
    ReadBreedResponse readBreed(Long breedId);
    UpdateBreedResponse updateBreed(Long breedId, UpdateBreedRequest request);
    void deleteBreed(Long breedId);
}
