package com.smartfarming.dto;

import lombok.Data;

@Data
public class QuestionRequest {
    private String title;
    private String content;
    private Long farmerId;
}
