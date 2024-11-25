package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.dto.request.widget.RequestSizeChatDto;
import com.example.webapplicationserver.service.OpenAIStreamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenAIStreamService openAIStreamService;

    @PostMapping(value = "size", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getSizeStreamChat(
            @RequestBody RequestSizeChatDto requestSizeChatDto
    ) {
        return openAIStreamService.streamChat(requestSizeChatDto);
    }

}
