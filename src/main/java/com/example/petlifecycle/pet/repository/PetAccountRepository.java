package com.example.petlifecycle.pet.repository;

import com.example.petlifecycle.pet.entity.PetAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetAccountRepository extends JpaRepository<PetAccount, Long> {
}
