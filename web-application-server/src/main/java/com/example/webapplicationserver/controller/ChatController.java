package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.dto.request.widget.RequestSizeChatDto;
import com.example.webapplicationserver.service.OpenAIStreamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenAIStreamService openAIStreamService;

    @PostMapping(value = "size", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getSizeStreamChat(
            @RequestBody RequestSizeChatDto requestSizeChatDto
    ) {
        return openAIStreamService.streamChat(requestSizeChatDto)
                .doOnCancel(() -> log.info("Connection canceled by client"))
                .doOnComplete(() -> log.info("Streaming completed"))
                .doOnError(e -> log.error("Streaming error", e));

    }

}
