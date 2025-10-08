package com.example.petlifecycle.cycle.repository;

import com.example.petlifecycle.cycle.domain.PetCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetCycleRepository extends JpaRepository<PetCycle, Long> {

    List<PetCycle> findAllByPetId(Long petId);
}
