package com.smartfarming.repository;

import com.smartfarming.model.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Long> {
    List<Fertilizer> findByRecommendedCropsContainingIgnoreCase(String cropName);
}
