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
    public ResponseEntity<Fertilizer> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock,
            @RequestParam Double price) {
        Fertilizer f = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found"));
        f.setStockBags(stock);
        f.setPricePerBag(price);
        return ResponseEntity.ok(fertilizerRepository.save(f));
    }
}
