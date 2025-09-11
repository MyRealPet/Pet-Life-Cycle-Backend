package com.example.petlifecycle.metadata.repository;

import com.example.petlifecycle.metadata.entity.MetaDataFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaDataFileRepository extends JpaRepository<MetaDataFile, Long> {
}
