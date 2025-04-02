package com.example.myapplication;

public class OpenAIRequest {
    private String model;
    private String prompt;
    private int max_tokens;

    public OpenAIRequest(String model, String prompt, int max_tokens) {
        this.model = model;
        this.prompt = prompt;
        this.max_tokens = max_tokens;
    }
}