package com.example.webapplicationserver.dto.openai;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private String model = "gpt-4o";
    private List<ChatMessage> messages;
    private boolean stream = true;
    private double temperature = 1;
}
