package com.smartfarming.controller;

import com.smartfarming.model.Disease;
import com.smartfarming.repository.DiseaseRepository;
import com.smartfarming.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGRONOMIST')")
    public Disease addDisease(@RequestBody Disease disease) {
        return diseaseRepository.save(disease);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Disease>> searchBySymptoms(@RequestParam String symptoms) {
        return ResponseEntity.ok(recommendationService.searchDiseaseBySymptoms(symptoms));
    }
}
