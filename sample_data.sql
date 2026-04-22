USE smartfarming;

-- Sample Crops
INSERT INTO Crop (name, suitable_soil_type, suitable_season, growing_duration_days, description) VALUES
('Maize', 'Loam', 'Summer', 120, 'High-yielding cereal crop, thrives in well-drained loamy soil.'),
('Rice', 'Clay', 'Rainy', 150, 'Requires flooded conditions and heavy clay soil.'),
('Wheat', 'Silt', 'Winter', 110, 'Temperate cereal grain suitable for cooler seasons.'),
('Potato', 'Sand', 'Spring', 90, 'Tuber crop that grows well in loose, sandy soil to allow expansion.');

-- Sample Diseases
INSERT INTO Disease (name, symptoms, treatment_suggestions) VALUES
('Maize Lethal Necrosis', 'Yellowing of leaves, stunting, terminal ear rot.', 'Use disease-resistant seeds, rotate crops, control pests like aphids.'),
('Potato Late Blight', 'Large, dark, water-soaked spots on leaves and stems.', 'Apply fungicide, remove infected plants, ensure proper spacing for airflow.'),
('Wheat Rust', 'Orange-red pustules on leaves and stems.', 'Plant resistant varieties, use appropriate foliar fungicides.');

-- Sample Fertilizers
INSERT INTO Fertilizer (name, type, recommended_crops, application_instructions) VALUES
('DAP', 'Inorganic', 'Maize, Wheat', 'Apply 50kg per acre during planting.'),
('Urea', 'Inorganic', 'Maize, Rice', 'Apply as top-dressing when plants reach 30cm height.'),
('Compost', 'Organic', 'Potato, All crops', 'Apply liberally before tilling soil.');
