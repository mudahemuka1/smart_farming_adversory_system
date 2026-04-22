package com.smartfarming.repository;

import com.smartfarming.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Safely fetches all questions only where the farmer still exists in DB
    @Query("SELECT q FROM Question q JOIN q.farmer f")
    List<Question> findAllWithExistingFarmer();

    // Correct JPA navigation: q.farmer.id (not a direct farmer_id column)
    List<Question> findByFarmer_Id(Long farmerId);
}
