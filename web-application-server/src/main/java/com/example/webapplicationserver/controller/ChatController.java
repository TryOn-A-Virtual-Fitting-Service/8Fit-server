package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.apiPayload.ApiResponseWrapper;
import com.example.webapplicationserver.apiPayload.code.status.SuccessStatus;
import com.example.webapplicationserver.dto.request.widget.RequestSizeChatDto;
import com.example.webapplicationserver.dto.response.chat.ResponseSizeChatDto;

import com.example.webapplicationserver.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenAIService openAIService;

//    @PostMapping(value = "size", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> getSizeStreamChat(
//            @RequestBody RequestSizeChatDto requestSizeChatDto
//    ) {
//        return openAIStreamService.streamChat(requestSizeChatDto)
//                .doOnCancel(() -> log.info("Connection canceled by client"))
//                .doOnComplete(() -> log.info("Streaming completed"))
//                .doOnError(e -> log.error("Streaming error", e));
//
//    }

    @PostMapping(value = "size")
    public ApiResponseWrapper<ResponseSizeChatDto> getSizeStreamChat(
            @RequestBody RequestSizeChatDto requestSizeChatDto
    ) {
        ResponseSizeChatDto responseSizeChatDto = openAIService.defaultChat(requestSizeChatDto);
        return ApiResponseWrapper.onSuccess(SuccessStatus.SIZE_CHAT_CREATED, responseSizeChatDto);

    }

}
