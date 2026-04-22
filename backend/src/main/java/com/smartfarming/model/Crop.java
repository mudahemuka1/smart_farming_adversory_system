package com.smartfarming.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Crop")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "suitable_soil_type", columnDefinition = "TEXT")
    private String suitableSoilType;

    @Column(name = "suitable_season", columnDefinition = "TEXT")
    private String suitableSeason;

    @Column(name = "growing_duration_days")
    private String growingDurationDays;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "full_texts", columnDefinition = "TEXT")
    private String fullTexts;
}
