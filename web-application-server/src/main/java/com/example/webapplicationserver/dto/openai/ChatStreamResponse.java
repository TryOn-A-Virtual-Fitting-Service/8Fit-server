package com.example.webapplicationserver.dto.openai;

import lombok.Data;

import java.util.List;

@Data
public class ChatStreamResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private String system_fingerprint; // 추가: system_fingerprint 필드
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Delta delta;
        private Object logprobs;
        private String finish_reason;

        @Data
        public static class Delta {
            private String role;
            private String content;
        }
    }
}
