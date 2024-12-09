package com.example.webapplicationserver.service;

import com.example.webapplicationserver.dto.openai.ChatDefaultResponse;
import com.example.webapplicationserver.dto.openai.ChatMessage;
import com.example.webapplicationserver.dto.openai.ChatRequest;
import com.example.webapplicationserver.dto.openai.ChatStreamResponse;
import com.example.webapplicationserver.dto.request.widget.RequestSizeChatDto;
import com.example.webapplicationserver.dto.response.chat.ResponseSizeChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIService {

    private final WebClient webClient;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    public ResponseSizeChatDto defaultChat(RequestSizeChatDto requestSizeChatDto) {
        // instruction
        ChatMessage instruction = new ChatMessage();
        instruction.setRole("system");
        instruction.setContent(
                "다음은 옷의 평균 사이즈야.\n" +
                        "| SIZE | 가슴둘레   | 어깨너비     | 소매길이     | 총길이        |\n" +
                        "|------|------------|-------------|--------------|--------------|\n" +
                        "| 90 S  | 90~96      | 43          | 59.5         | 73           |\n" +
                        "| 95 M  | 95~100     | 43~45       | 60.5         | 69~74        |\n" +
                        "| 100 L | 100~104    | 44.5~47     | 61.5~62      | 71~75        |\n" +
                        "| 105 XL | 105~108    | 46~49       | 62.5~63.5    | 73~76        |\n" +
                        "| 110 XXL | 110~112   | 47.5~51     | 63.5~65      | 75~77        |\n" +
                        "사용자한테 일반적인 옷 평균 수치(방금 전에 보낸 값)에 비해 어깨너비나 등등 다 어떻게 다른지해서 오버핏인지 슬림핏인지 그런 것들을 판단해서 서브 메시지를 줘서 사이즈 판단에 도움을 주려해 거기에 필요한 글을 4줄 정도 써줄래? \n" +
                        "앞에다가 이 부분은 일반적인 사이즈보다는 몇 cm~ 정도는 더 크게 나온 제품이다. 이런 정보도 섞어줄 수 있어?\n" +
                        "경향은 다 동일하니까 특정 사이즈를 기준으로 말할 필요는 없어."
        );
        // 사용자 메시지
        ChatMessage userInput = new ChatMessage();
        userInput.setRole("user");
        userInput.setContent(requestSizeChatDto.content());

        // 요청 객체 생성
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(instruction, userInput));

        ChatDefaultResponse chatDefaultResponse = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatDefaultResponse.class)
                .block();

        return new ResponseSizeChatDto(chatDefaultResponse.getChoices().getFirst().getMessage().getContent());

    }

    public Flux<String> streamChat(RequestSizeChatDto requestSizeChatDto) {
        // instruction
        ChatMessage instruction = new ChatMessage();
        instruction.setRole("system");
        instruction.setContent(
                "다음은 옷의 평균 사이즈야.\n" +
                "| SIZE | 가슴둘레   | 어깨너비     | 소매길이     | 총길이        |\n" +
                "|------|------------|-------------|--------------|--------------|\n" +
                "| 90 S  | 90~96      | 43          | 59.5         | 73           |\n" +
                "| 95 M  | 95~100     | 43~45       | 60.5         | 69~74        |\n" +
                "| 100 L | 100~104    | 44.5~47     | 61.5~62      | 71~75        |\n" +
                "| 105 XL | 105~108    | 46~49       | 62.5~63.5    | 73~76        |\n" +
                "| 110 XXL | 110~112   | 47.5~51     | 63.5~65      | 75~77        |\n" +
                "사용자한테 일반적인 옷 평균 수치(방금 전에 보낸 값)에 비해 어깨너비나 등등 다 어떻게 다른지해서 오버핏인지 슬림핏인지 그런 것들을 판단해서 서브 메시지를 줘서 사이즈 판단에 도움을 주려해 거기에 필요한 글을 4줄 정도 써줄래? \n" +
                "앞에다가 이 부분은 일반적인 사이즈보다는 몇 cm~ 정도는 더 크게 나온 제품이다. 이런 정보도 섞어줄 수 있어?\n" +
                "경향은 다 동일하니까 특정 사이즈를 기준으로 말할 필요는 없어. 너가 쇼핑 어시스턴트인 말투로 대답해"
        );

        // 사용자 메시지
        ChatMessage userInput = new ChatMessage();
        userInput.setRole("user");
        userInput.setContent(requestSizeChatDto.content());

        // 요청 객체 생성
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(instruction, userInput));

        log.debug(request.toString());

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class) // 청크 데이터를 문자열로 수신
                .flatMap(chunk -> {

                    if (chunk.equals("[DONE]")) {
                        return Mono.just("[DONE]");
                    }

                    try {
                        return Mono.justOrEmpty(parseChunk(chunk)); // 파싱 성공 시 데이터 반환
                    } catch (Exception e) {
//                        log.error("Error parsing chunk: " + chunk); // 디버깅용 로그
                        return Mono.empty(); // 에러 발생 시 해당 청크 무시
                    }
                })
                .filter(content -> content != null && !content.isEmpty()) // 유효한 데이터만 필터링
                .concatWith(Flux.just(" ")) // Keep-Alive를 위한 빈 데이터 추가
                .onErrorResume(error -> {
//                    log.error("Streaming error occurred", error);
                    return Flux.empty(); // 에러 발생 시 빈 스트림 반환
                });
//                .doOnNext(content -> log.info("Parsed content: " + content)); // 디버깅용
    }

    private String parseChunk(String chunk) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatStreamResponse response = objectMapper.readValue(chunk, ChatStreamResponse.class);

            // choices 배열이 비어있거나 null인 경우 처리
            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                ChatStreamResponse.Choice choice = response.getChoices().getFirst(); // 첫 번째 choice 가져오기
                if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                    return choice.getDelta().getContent(); // content 반환
                }
            }
        } catch (Exception e) {
            log.error("Error parsing chunk: " + chunk); // 디버깅용 로그
        }
        return null; // 파싱 실패 시 null 반환
    }



}
