package com.example.petlifecycle.vaccine.repository;

import com.example.petlifecycle.breed.entity.Species;
import com.example.petlifecycle.vaccine.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    List<Vaccine> findBySpecies(Species s);
}
