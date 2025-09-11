package com.example.petlifecycle.breed.service;

import com.example.petlifecycle.breed.controller.request.ListBreedRequest;
import com.example.petlifecycle.breed.controller.request.RegisterBreedRequest;
import com.example.petlifecycle.breed.controller.response.ListBreedResponse;
import com.example.petlifecycle.breed.controller.response.RegisterBreedResponse;
import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;

    @Override
    public RegisterBreedResponse registerBreed(RegisterBreedRequest request) {

        Breed savedBreed = breedRepository.save(request.toBreed());
        return RegisterBreedResponse.from(savedBreed);
    }

    @Override
    public ListBreedResponse getAllBreed(ListBreedRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;  // 0-based page index
        int perPage = request.getPerPage() > 0 ? request.getPerPage() : 10;

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Breed> paginatedlaptopList = breedRepository.findAllByIsDeletedFalse(pageable);

        List<Breed> breedList = paginatedlaptopList.getContent();

        return ListBreedResponse.from(
                breedList,
                paginatedlaptopList.getNumber() + 1,
                paginatedlaptopList.getTotalPages(),
                paginatedlaptopList.getTotalElements()
        );
    }

}

