package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.records.FigmaDocument;
import br.com.fiap.MiaDBD.records.FigmaFileResponse;
import br.com.fiap.MiaDBD.records.FigmaImagesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MiaFigmaService {

    private WebClient webClient;

    public MiaFigmaService(WebClient.Builder builder,
                           @Value("${figma.api.key}") String apiKey) {
        this.webClient = builder
                .baseUrl("https://api.figma.com/v1")
                .defaultHeader("X-FIGMA-TOKEN", String.format("%s", apiKey))
                .build();
    }

    public Mono<FigmaImagesResponse> getFigmaImage(String fileId, String nodeId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/images/{fileId}")
                        .queryParam("ids", nodeId)
                        .build(fileId))
                .retrieve()
                .bodyToMono(FigmaImagesResponse.class);
    }

    public Mono<FigmaFileResponse> getFigmaFileNodesIds(String fileId) {
        return webClient.get()
                .uri("/files/{fileId}", fileId)
                .retrieve()
                .bodyToMono(FigmaFileResponse.class);
    }
}
