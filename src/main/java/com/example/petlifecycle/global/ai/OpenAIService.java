package com.example.petlifecycle.global.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final WebClient webClient;
    private final OpenAIProperties openAIProperties;

    // WebClient를 생성자 주입으로 구성
    public OpenAIService(OpenAIProperties openAIProperties) {
        this.openAIProperties = openAIProperties;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * AI에게 질문을 보내고 답변을 받아오는 함수
     */
    public Mono<String> getChatResponse(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", openAIProperties.getModel(),
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        return webClient.post()
                .uri(openAIProperties.getUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAIProperties.getApiKey())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
