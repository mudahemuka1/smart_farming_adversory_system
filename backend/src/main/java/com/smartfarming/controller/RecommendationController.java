package com.smartfarming.controller;

import com.smartfarming.model.Recommendation;
import com.smartfarming.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping
    @PreAuthorize("hasRole('AGRONOMIST') or hasRole('ADMIN')")
    public ResponseEntity<Recommendation> createRecommendation(
            @RequestParam Long farmerId,
            @RequestParam(required = false) Long cropId,
            @RequestParam(required = false) Long diseaseId,
            @RequestParam(required = false) Long fertilizerId,
            @RequestBody String advice) {
        return ResponseEntity.ok(recommendationService.createRecommendation(farmerId, cropId, diseaseId, fertilizerId, advice));
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Recommendation> getFarmerRecommendations(@PathVariable Long farmerId) {
        return recommendationService.getFarmerRecommendations(farmerId);
    }
}
