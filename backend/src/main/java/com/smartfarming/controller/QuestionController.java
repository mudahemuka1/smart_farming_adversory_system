package com.smartfarming.controller;

import com.smartfarming.dto.AnswerRequest;
import com.smartfarming.dto.QuestionRequest;
import com.smartfarming.model.Answer;
import com.smartfarming.model.Question;
import com.smartfarming.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @PostMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<Question> askQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.askQuestion(request.getFarmerId(), request.getTitle(), request.getContent()));
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Question> getFarmerQuestions(@PathVariable Long farmerId) {
        return questionService.getFarmerQuestions(farmerId);
    }

    @PostMapping("/{questionId}/answers")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<Answer> answerQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest request) {
        return ResponseEntity.ok(questionService.answerQuestion(questionId, request.getAgronomistId(), request.getContent()));
    }
}
