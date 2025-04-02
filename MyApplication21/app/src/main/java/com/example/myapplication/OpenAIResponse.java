package com.example.myapplication;

import java.util.List;

public class OpenAIResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message; // 假設 API 返回 "message" 字段
    }

    public static class Message {
        public String role;
        public String content;
    }
}
