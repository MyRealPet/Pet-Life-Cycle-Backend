package com.example.petlifecycle.ai.dto;

import lombok.Getter;

@Getter
public class AiResponse {
    private String answer;

    public AiResponse(String answer) {
        this.answer = answer;
    }
}
