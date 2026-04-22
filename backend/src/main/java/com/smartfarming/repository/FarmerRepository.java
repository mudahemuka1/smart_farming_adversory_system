package com.smartfarming.repository;

import com.smartfarming.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    List<Farmer> findByLocationContainingIgnoreCase(String location);
}
