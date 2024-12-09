package com.example.webapplicationserver.dto.openai;

import lombok.Data;
import java.util.List;

@Data
public class ChatDefaultResponse {
    // Getter, Setter
    private List<Choice> choices;

    // Choice 내부 클래스
    @Data
    public static class Choice {
        private ChatMessage message;

    }
}
