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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGRONOMIST')")
    public ResponseEntity<Disease> updateDisease(@PathVariable Long id, @RequestBody Disease diseaseDetails) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));
        
        disease.setName(diseaseDetails.getName());
        disease.setSymptoms(diseaseDetails.getSymptoms());
        disease.setTreatmentSuggestions(diseaseDetails.getTreatmentSuggestions());
        
        return ResponseEntity.ok(diseaseRepository.save(disease));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDisease(@PathVariable Long id) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));
        diseaseRepository.delete(disease);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Disease>> searchBySymptoms(@RequestParam String symptoms) {
        return ResponseEntity.ok(recommendationService.searchDiseaseBySymptoms(symptoms));
    }
}
