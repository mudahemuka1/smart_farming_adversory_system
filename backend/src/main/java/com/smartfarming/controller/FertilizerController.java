package com.smartfarming.controller;

import com.smartfarming.model.Fertilizer;
import com.smartfarming.repository.FertilizerRepository;
import com.smartfarming.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fertilizers")
public class FertilizerController {

    @Autowired
    private FertilizerRepository fertilizerRepository;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<Fertilizer> getAllFertilizers() {
        return fertilizerRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGRO_DEALER')")
    public Fertilizer addFertilizer(@RequestBody Fertilizer fertilizer) {
        return fertilizerRepository.save(fertilizer);
    }

    @GetMapping("/by-crop")
    public List<Fertilizer> getByCrop(@RequestParam String cropName) {
        return recommendationService.getFertilizersForCrop(cropName);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGRO_DEALER')")
    public ResponseEntity<Fertilizer> updateFertilizer(@PathVariable Long id, @RequestBody Fertilizer fertilizerDetails) {
        Fertilizer fertilizer = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with id: " + id));
        
        fertilizer.setName(fertilizerDetails.getName());
        fertilizer.setType(fertilizerDetails.getType());
        fertilizer.setRecommendedCrops(fertilizerDetails.getRecommendedCrops());
        fertilizer.setApplicationInstructions(fertilizerDetails.getApplicationInstructions());
        fertilizer.setStockBags(fertilizerDetails.getStockBags());
        fertilizer.setPricePerBag(fertilizerDetails.getPricePerBag());
        
        return ResponseEntity.ok(fertilizerRepository.save(fertilizer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGRO_DEALER')")
    public ResponseEntity<?> deleteFertilizer(@PathVariable Long id) {
        Fertilizer fertilizer = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with id: " + id));
        fertilizerRepository.delete(fertilizer);
        return ResponseEntity.ok().build();
    }
}
