package com.example.petlifecycle.health.weight.repository;

import com.example.petlifecycle.health.weight.domain.PetWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetWeightRepository extends JpaRepository<PetWeight, Long> {

    List<PetWeight> findAllByPetIdOrderByRecordDateAsc(Long petId);
}
