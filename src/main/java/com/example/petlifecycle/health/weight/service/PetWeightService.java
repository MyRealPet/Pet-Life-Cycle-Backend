package com.example.petlifecycle.health.weight.service;

import com.example.petlifecycle.health.weight.domain.PetWeight;
import com.example.petlifecycle.health.weight.dto.PetWeightCreateRequest;
import com.example.petlifecycle.health.weight.dto.PetWeightResponse;
import com.example.petlifecycle.health.weight.repository.PetWeightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetWeightService {

    private final PetWeightRepository petWeightRepository;

    @Transactional
    public void saveWeight(Long petId, PetWeightCreateRequest request) {
        // 요청 날짜가 없으면 오늘 날짜로 기록
        LocalDate recordDate = request.getRecordDate() != null ? request.getRecordDate() : LocalDate.now();
        PetWeight petWeight = PetWeight.create(petId, request.getWeight(), recordDate);
        petWeightRepository.save(petWeight);
    }

    public List<PetWeightResponse> findWeights(Long petId) {
        return petWeightRepository.findAllByPetIdOrderByRecordDateAsc(petId).stream()
                .map(PetWeightResponse::from)
                .collect(Collectors.toList());
    }
}
