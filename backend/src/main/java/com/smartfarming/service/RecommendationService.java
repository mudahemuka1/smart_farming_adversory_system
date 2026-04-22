package com.smartfarming.service;

import com.smartfarming.model.*;
import com.smartfarming.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private FertilizerRepository fertilizerRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public List<Crop> recommendCrops(String soilType, String season) {
        return cropRepository.findBySuitableSoilTypeContainingIgnoreCaseAndSuitableSeasonContainingIgnoreCase(soilType, season);
    }

    public List<Disease> searchDiseaseBySymptoms(String symptoms) {
        return diseaseRepository.findBySymptomsContainingIgnoreCase(symptoms);
    }

    public List<Fertilizer> getFertilizersForCrop(String cropName) {
        return fertilizerRepository.findByRecommendedCropsContainingIgnoreCase(cropName);
    }

    public Recommendation createRecommendation(Long farmerId, Long cropId, Long diseaseId, Long fertilizerId, String advice) {
        Farmer farmer = farmerRepository.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer not found"));
        
        Recommendation rec = new Recommendation();
        rec.setFarmer(farmer);
        if (cropId != null) rec.setCrop(cropRepository.findById(cropId).orElse(null));
        if (diseaseId != null) rec.setDisease(diseaseRepository.findById(diseaseId).orElse(null));
        if (fertilizerId != null) rec.setFertilizer(fertilizerRepository.findById(fertilizerId).orElse(null));
        rec.setAdviceText(advice);

        return recommendationRepository.save(rec);
    }

    public List<Recommendation> getFarmerRecommendations(Long farmerId) {
        return recommendationRepository.findByFarmerId(farmerId);
    }
}
