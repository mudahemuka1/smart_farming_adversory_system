CREATE DATABASE IF NOT EXISTS smartfarming;
USE smartfarming;

-- Drop tables if they exist to allow clean re-runs
DROP TABLE IF EXISTS Answer;
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS Recommendation;
DROP TABLE IF EXISTS AgroDealer;
DROP TABLE IF EXISTS Agronomist;
DROP TABLE IF EXISTS Fertilizer;
DROP TABLE IF EXISTS Disease;
DROP TABLE IF EXISTS Crop;
DROP TABLE IF EXISTS Farmer;
DROP TABLE IF EXISTS User;

-- 1. User Table (Base table for authentication & RBAC)
CREATE TABLE User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('FARMER', 'ADMIN', 'AGRONOMIST', 'AGRO_DEALER') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    verification_code VARCHAR(10),
    verification_code_expires_at DATETIME,
    last_login_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_email ON User(email);

-- 2. Farmer Table
CREATE TABLE Farmer (
    id BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    contact_number VARCHAR(20),
    FOREIGN KEY (id) REFERENCES User(id) ON DELETE CASCADE
);

-- 3. Agronomist Table
CREATE TABLE Agronomist (
    id BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    contact_number VARCHAR(20),
    FOREIGN KEY (id) REFERENCES User(id) ON DELETE CASCADE
);

-- 4. AgroDealer Table
CREATE TABLE AgroDealer (
    id BIGINT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    contact_number VARCHAR(20),
    full_texts TEXT,
    FOREIGN KEY (id) REFERENCES User(id) ON DELETE CASCADE
);

-- 5. Crop Table
CREATE TABLE Crop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    suitable_soil_type VARCHAR(50) NOT NULL,
    suitable_season VARCHAR(50) NOT NULL,
    growing_duration_days INT,
    description TEXT,
    full_texts TEXT
);
CREATE INDEX idx_crop_soil_season ON Crop(suitable_soil_type, suitable_season);

-- 6. Disease Table
CREATE TABLE Disease (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    symptoms TEXT NOT NULL,
    treatment_suggestions TEXT
);
CREATE INDEX idx_disease_name ON Disease(name);

-- 7. Fertilizer Table
CREATE TABLE Fertilizer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50), 
    recommended_crops TEXT, 
    application_instructions TEXT
);

-- 8. Recommendation Table (System generated advice)
CREATE TABLE Recommendation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    crop_id BIGINT,
    disease_id BIGINT,
    fertilizer_id BIGINT,
    advice_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (farmer_id) REFERENCES Farmer(id) ON DELETE CASCADE,
    FOREIGN KEY (crop_id) REFERENCES Crop(id) ON DELETE SET NULL,
    FOREIGN KEY (disease_id) REFERENCES Disease(id) ON DELETE SET NULL,
    FOREIGN KEY (fertilizer_id) REFERENCES Fertilizer(id) ON DELETE SET NULL
);

-- 9. Question Table (Community interaction)
CREATE TABLE Question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    is_resolved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (farmer_id) REFERENCES Farmer(id) ON DELETE CASCADE
);

-- 10. Answer Table (Community interaction)
CREATE TABLE Answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    agronomist_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES Question(id) ON DELETE CASCADE,
    FOREIGN KEY (agronomist_id) REFERENCES Agronomist(id) ON DELETE CASCADE
);
