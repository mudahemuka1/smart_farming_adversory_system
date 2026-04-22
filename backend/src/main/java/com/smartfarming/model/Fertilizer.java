package com.smartfarming.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Fertilizer")
public class Fertilizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(name = "recommended_crops", columnDefinition = "TEXT")
    private String recommendedCrops;

    @Column(name = "application_instructions", columnDefinition = "TEXT")
    private String applicationInstructions;

    @Column(name = "stock_bags")
    private Integer stockBags;

    @Column(name = "price_per_bag")
    private Double pricePerBag;
}
