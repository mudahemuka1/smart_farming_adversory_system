package com.smartfarming.service;

import com.smartfarming.model.Answer;
import com.smartfarming.model.Question;
import com.smartfarming.model.Agronomist;
import com.smartfarming.model.Farmer;
import com.smartfarming.repository.AnswerRepository;
import com.smartfarming.repository.QuestionRepository;
import com.smartfarming.repository.AgronomistRepository;
import com.smartfarming.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private AgronomistRepository agronomistRepository;

    public Question askQuestion(Long farmerId, String title, String content) {
        Farmer farmer = farmerRepository.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer not found"));
        Question question = new Question();
        question.setFarmer(farmer);
        question.setTitle(title);
        question.setContent(content);
        return questionRepository.save(question);
    }

    public Answer answerQuestion(Long questionId, Long agronomistId, String content) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        Agronomist agronomist = agronomistRepository.findById(agronomistId).orElseThrow(() -> new RuntimeException("Agronomist not found"));
        
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setAgronomist(agronomist);
        answer.setContent(content);
        
        question.setIsResolved(true);
        questionRepository.save(question);
        
        return answerRepository.save(answer);
    }

    public List<Question> getAllQuestions() {
        // Use safe JOIN query to avoid EntityNotFoundException on orphaned farmer records
        return questionRepository.findAllWithExistingFarmer();
    }

    public List<Question> getFarmerQuestions(Long farmerId) {
        return questionRepository.findByFarmer_Id(farmerId);
    }
}
