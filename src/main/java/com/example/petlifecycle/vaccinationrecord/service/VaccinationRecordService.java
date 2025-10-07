package com.example.petlifecycle.vaccinationrecord.service;

import com.example.petlifecycle.breed.entity.Breed;
import com.example.petlifecycle.breed.repository.BreedRepository;
import com.example.petlifecycle.pet.entity.PetAccount;
import com.example.petlifecycle.pet.service.PetAccountService;
import com.example.petlifecycle.vaccinationrecord.controller.request.RegisterVacRecordRequest;
import com.example.petlifecycle.vaccinationrecord.controller.request.UpdateVacRecordRequest;
import com.example.petlifecycle.vaccinationrecord.controller.response.ReadVacRecordResponse;
import com.example.petlifecycle.vaccinationrecord.entity.VaccinationRecord;
import com.example.petlifecycle.vaccinationrecord.repository.VaccinationRecordRepository;
import com.example.petlifecycle.vaccine.entity.Vaccine;
import com.example.petlifecycle.vaccine.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VaccinationRecordService {

    private final PetAccountService petAccountService;
    private final BreedRepository breedRepository;
    private final VaccineRepository vaccineRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;

    public void registerVacRecord(Long accountId, Long petId, RegisterVacRecordRequest request) {
        petAccountService.validateAndGetPetAccount(petId, accountId);

        validateVacConstraints(request.getVaccineId(), request.getCustomVaccineName());

        VaccinationRecord vacRecord = request.toVaccinationRecord(petId);
        vaccinationRecordRepository.save(vacRecord);
    }

    public ReadVacRecordResponse readVacRecord(Long accountId, Long petId, Long recordId) {
        petAccountService.validateAndGetPetAccount(petId, accountId);
        VaccinationRecord vacRecord = vaccinationRecordRepository.findByIdAndIsDeletedFalse(recordId)
                .orElseThrow(() -> new RuntimeException("접종 이력을 찾을 수 없습니다."));
        String vaccineName = null;
        if (vacRecord.getVaccineId() != null) {
            Vaccine vaccine = vaccineRepository.findById(vacRecord.getVaccineId())
                    .orElseThrow(() -> new RuntimeException("백신 정보를 찾는데 실패했습니다."));
            vaccineName = vaccine.getVaccineName();
        } else {
            vaccineName = vacRecord.getCustomVaccineName();
        }
        return ReadVacRecordResponse.from(vacRecord, vaccineName);
    }

    public void updateVaccinationRecord(Long accountId, Long petId, Long recordId, UpdateVacRecordRequest request) {
        petAccountService.validateAndGetPetAccount(petId, accountId);

        VaccinationRecord vacRecord = vaccinationRecordRepository.findByIdAndIsDeletedFalse(recordId)
                .orElseThrow(() -> new RuntimeException("접종 이력을 찾는데 실패했습니다."));

        validateVacConstraints(vacRecord.getVaccineId(), request.getCustomVaccineName());

        vacRecord.update(request.getCustomVaccineName(), request.getVaccinationDate(), request.getHospitalName());
    }

    private void validateVacConstraints(Long vaccineId, String customVaccineName) {
        boolean hasVac = (vaccineId != null && vaccineId != 0);
        boolean hascustomVac = (customVaccineName != null && !customVaccineName.trim().isEmpty());

        if (!hasVac && !hascustomVac) {
            throw new RuntimeException("접종 백신의 정보를 입력해주세요.");
        }
        if (hasVac && hascustomVac) {
            throw new RuntimeException("등록된 백신 정보 입력 시, 직접입력은 할 수 없습니다.");
        }

        if (hasVac) {
            Vaccine vaccine = vaccineRepository.findById(vaccineId)
                    .orElseThrow(() -> new RuntimeException("백신 정보를 찾는데 실패했습니다."));
        }
    }
}
