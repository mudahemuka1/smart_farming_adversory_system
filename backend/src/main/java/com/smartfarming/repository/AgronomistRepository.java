package com.smartfarming.repository;

import com.smartfarming.model.Agronomist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgronomistRepository extends JpaRepository<Agronomist, Long> {
}
