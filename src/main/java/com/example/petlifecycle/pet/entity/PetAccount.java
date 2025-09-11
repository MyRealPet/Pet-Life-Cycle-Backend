package com.example.petlifecycle.pet.entity;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class PetAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;
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

    private Double weight;
    private Boolean isNeutered;
    private Boolean hasMicrochip;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_img_id")
    private MetaDataFile profileImg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_pdf_id")
    private MetaDataFile registrationPdf;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public PetAccount(String name, Breed mainBreed, Breed subBreed, String gender, LocalDate birthday, Double weight, Boolean isNeutered, Boolean hasMicrochip) {
        this.name = name;
        this.mainBreed = mainBreed;
        this.subBreed = subBreed;
        this.gender = gender;
        this.birthday = birthday;
        this.weight = weight;
        this.isNeutered = isNeutered;
        this.hasMicrochip = hasMicrochip;
    }

    public void setProfileImg(MetaDataFile profileImg) {
        this.profileImg = profileImg;
    }

    public void setRegistrationPdf(MetaDataFile registrationPdf) {
        this.registrationPdf = registrationPdf;
    }

    public void delete() {
        isDeleted = true;
    }
}

