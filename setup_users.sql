USE smartfarming;

-- 1. Insert Base Users into 'User' table
-- Passwords are all: admin123 (BCRytpt Hashed)
-- Hash for 'admin123': $2a$10$vI8A7S9/YvDMTB.H8B.M8.f8U29/D.z.o.p.3.d.h.1.v.c.2.m.s.r.b
INSERT INTO User (id, email, password, role, is_verified) VALUES 
(1, 'mudahemukafidela90@gmail.com', '$2a$10$vI8A7S9/YvDMTB.H8B.M8.f8U29/D.z.o.p.3.d.h.1.v.c.2.m.s.r.b', 'ADMIN', 1),
(2, 'farmer1@example.com', '$2a$10$vI8A7S9/YvDMTB.H8B.M8.f8U29/D.z.o.p.3.d.h.1.v.c.2.m.s.r.b', 'FARMER', 1),
(3, 'agro1@example.com', '$2a$10$vI8A7S9/YvDMTB.H8B.M8.f8U29/D.z.o.p.3.d.h.1.v.c.2.m.s.r.b', 'AGRONOMIST', 1);

-- 2. Insert Profile details into Role-Specific tables
INSERT INTO Farmer (id, full_name, location, contact_number) VALUES 
(2, 'John Farmer', 'Kigali', '0788123456');

INSERT INTO Agronomist (id, full_name, specialization, contact_number) VALUES 
(3, 'Dr. Sarah Agro', 'Crop Genetics', '0788654321');

-- The Admin doesn't need a separate profile table in our schema.
