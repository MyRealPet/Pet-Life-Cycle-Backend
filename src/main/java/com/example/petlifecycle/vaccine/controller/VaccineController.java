package com.example.petlifecycle.vaccine.controller;

import com.example.petlifecycle.vaccine.dto.VaccineRequest;
import com.example.petlifecycle.vaccine.dto.VaccineResponse;
import com.example.petlifecycle.vaccine.service.VaccineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccine")
public class VaccineController {
    private final VaccineService vaccineService;

    @PostMapping
    public VaccineResponse createVaccine(@RequestBody VaccineRequest request) {
        return VaccineResponse.fromVaccine(vaccineService.createVaccine(request.toEntity()));
    }

    @GetMapping
    public List<VaccineResponse> getAllVaccines() {
        return vaccineService.getAllVaccines()
                .stream()
                .map(VaccineResponse::fromVaccine)
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}")
    public VaccineResponse getVaccineById(@PathVariable long id) {
        return VaccineResponse.fromVaccine(vaccineService.getVaccineById(id));
    }

    //종별 백신 조회
    @GetMapping("/species/{species}")
    public List<VaccineResponse> getVaccineBySpecies(@PathVariable String species) {
        return vaccineService.getVaccineBySpecies(species)
                .stream()
                .map(VaccineResponse::fromVaccine)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public VaccineResponse updateVaccine(@PathVariable Long id, @RequestBody VaccineRequest request) {
        return VaccineResponse.fromVaccine(vaccineService.updateVaccine(id, request.toEntity()));
    }

    @DeleteMapping("/{id}")
    public void deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
    }

}
