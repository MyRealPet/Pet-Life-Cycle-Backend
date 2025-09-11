package com.example.petlifecycle.breed.repository;

import com.example.petlifecycle.breed.entity.Breed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByIdAndIsDeletedFalse(Long id);
    Page<Breed> findAllByIsDeletedFalse(Pageable pageable);
}
