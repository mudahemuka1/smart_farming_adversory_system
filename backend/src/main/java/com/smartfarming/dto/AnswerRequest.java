package com.smartfarming.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private String content;
    private Long agronomistId;
}
