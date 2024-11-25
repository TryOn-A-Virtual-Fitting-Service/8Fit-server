package com.example.webapplicationserver.dto.openai;

import lombok.Data;

@Data
public class ChatMessage {
    private String role;
    private String content;
}
