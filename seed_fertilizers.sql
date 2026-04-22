USE smartfarming;

-- Create some default fertilizers for AgroDealers
INSERT INTO fertilizer (name, type, recommended_crops, application_instructions, stock_bags, price_per_bag)
VALUES ('Maize Urea', 'Chemical', 'Maize, Sorghum', 'Apply 50kg per hectare at planting.', 450, 15000)
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO fertilizer (name, type, recommended_crops, application_instructions, stock_bags, price_per_bag)
VALUES ('Rice DAP', 'Chemical', 'Rice', 'Use 100kg per hectare during land prep.', 120, 22000)
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO fertilizer (name, type, recommended_crops, application_instructions, stock_bags, price_per_bag)
VALUES ('Organic Compost', 'Organic', 'Vegetables, Coffee', 'Apply 2 tons per hectare annually.', 1200, 5000)
ON DUPLICATE KEY UPDATE name=name;
