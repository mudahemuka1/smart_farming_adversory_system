package com.smartfarming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.smartfarming.model.Question;
import com.smartfarming.repository.QuestionRepository;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting data cleanup...");
        try {
            List<Question> questions = questionRepository.findAll();
            // We just need to catch the EntityNotFoundException that gets thrown by hibernate when fetching lazy proxies,
            // or just iterate through and delete the bad ones manually.
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
