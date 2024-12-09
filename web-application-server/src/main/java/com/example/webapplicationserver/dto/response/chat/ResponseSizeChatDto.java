package com.example.webapplicationserver.dto.response.chat;

import com.example.webapplicationserver.dto.openai.ChatDefaultResponse;
import com.example.webapplicationserver.dto.openai.ChatStreamResponse;

public record ResponseSizeChatDto(
        String sizeChat
        ) {

}
