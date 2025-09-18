package com.example.petlifecycle.pet.entity;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.pet.controller.request.UpdatePetAccountRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PetAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_breed_id", nullable = false)
    private Breed mainBreed;
    // null 이거나 mainBreed 와 달라야 함 (믹스견)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_breed_id")
    private Breed subBreed;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private Boolean isNeutered;
    private Boolean hasMicrochip;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_img_id")
    private MetaDataFile profileImg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_pdf_id")
    private MetaDataFile registrationPdf;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public PetAccount(Long accountId, String name, Breed mainBreed, Breed subBreed, String gender, LocalDate birthday, Boolean isNeutered, Boolean hasMicrochip) {
        this.accountId = accountId;
        this.name = name;
        this.mainBreed = mainBreed;
        this.subBreed = subBreed;
        this.gender = gender;
        this.birthday = birthday;
        this.isNeutered = isNeutered;
        this.hasMicrochip = hasMicrochip;
    }

    public void setMainBreed(Breed mainBreed) { this.mainBreed = mainBreed; }

    public void setSubBreed(Breed subBreed) { this.subBreed = subBreed; }

    public void setProfileImg(MetaDataFile profileImg) {
        this.profileImg = profileImg;
    }

    public void setRegistrationPdf(MetaDataFile registrationPdf) {
        this.registrationPdf = registrationPdf;
    }

    public void update(UpdatePetAccountRequest request) {
        this.name = request.getName();
        this.gender = request.getGender();
        this.birthday = request.getBirthday();
        this.isNeutered = request.getIsNeutered();
        this.hasMicrochip = request.getHasMicrochip();
    }

    public void delete() {
        isDeleted = true;
        deletedAt = LocalDateTime.now();
    }
}

