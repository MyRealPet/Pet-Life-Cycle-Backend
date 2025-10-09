package com.example.petlifecycle.cycle.service;

import com.example.petlifecycle.cycle.domain.PetCycle;
import com.example.petlifecycle.cycle.dto.PetCycleCreateRequest;
import com.example.petlifecycle.cycle.dto.PetCycleResponse;
import com.example.petlifecycle.cycle.repository.PetCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetCycleService {

    private final PetCycleRepository petCycleRepository;

    @Transactional
    public void saveCycle(Long petId, PetCycleCreateRequest request) {
        PetCycle petCycle = PetCycle.create(petId,
                request.getStartDate(),
                request.getEndDate(),
                request.getMemo());
        petCycleRepository.save(petCycle);
    }

    public List<PetCycleResponse> findCycles(Long petId) {
        return petCycleRepository.findAllByPetId(petId).stream()
                .map(PetCycleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCycle(Long cycleId) {
        if (!petCycleRepository.existsById(cycleId)) {
            throw new IllegalArgumentException("존재하지 않는 기록입니다.");
        }
        petCycleRepository.deleteById(cycleId);
    }
}
