package com.smartfarming.repository;

import com.smartfarming.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findBySymptomsContainingIgnoreCase(String symptoms);
}
