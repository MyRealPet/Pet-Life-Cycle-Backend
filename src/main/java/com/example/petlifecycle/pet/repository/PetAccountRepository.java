package com.example.petlifecycle.pet.repository;

import com.example.petlifecycle.pet.entity.PetAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PetAccountRepository extends JpaRepository<PetAccount, Long> {

    Optional<PetAccount> findByPetIdAndIsDeletedFalse(Long petId);
    List<PetAccount> findByAccountIdAndIsDeletedFalseOrderByCreatedAtDesc(Long accountId);
}
