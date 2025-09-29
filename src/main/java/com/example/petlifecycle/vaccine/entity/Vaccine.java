package com.example.petlifecycle.vaccine.entity;

import com.example.petlifecycle.breed.entity.Species;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vaccineId;

    @Enumerated(EnumType.STRING)
    private Species species;

    private String vaccineName;
    private String description;
    private String sideEffects;     //부작용
    private String vaccineCycle;    //백신주기

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
