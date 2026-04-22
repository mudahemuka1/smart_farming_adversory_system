package com.smartfarming.repository;

import com.smartfarming.model.AgroDealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgroDealerRepository extends JpaRepository<AgroDealer, Long> {
}
