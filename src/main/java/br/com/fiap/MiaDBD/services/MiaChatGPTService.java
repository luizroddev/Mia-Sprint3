package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.records.ChatGPTResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class MiaChatGPTService {

    private WebClient webClient;

    public MiaChatGPTService(WebClient.Builder builder,
                             @Value("${openai.api.key}") String apiKey) {

        this.webClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", String.format("Bearer %s", apiKey))
                .build();
    }

    public Mono<ChatGPTResponse> createTaskExplanation(String question) {
        ChatGPTRequest request = createTaskRequest(question);

        return webClient.post().bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Error during the request. Status code: " + response)))
                .bodyToMono(ChatGPTResponse.class);
    }

    private ChatGPTRequest createTaskRequest(String question) {
        String context = "Explique passo a passo como executar uma tarefa dentro de um aplicativo\n" +
                "Cite todas as telas e os elementos que serão necessários ser interagidos\n" +
                "Apresente na resposta uma array da seguinte forma: [nomeDoAplicativo-nomeDaTelaAtual_nomeDoElementoInteragido]\n" +
                "Apresente também a explicação do passo a passo, separando os passos por - como prefixo, e no fim da frase adicionando " +
                "em ordem os indíces dos elementos da array que foram usados naquela explicação de passo da seguinte forma (2, 3, 4)\n";

        String exampleQuestion = "Como abrir o meu perfil no Whatsapp?";
        String exampleAnswer = "Exemplo de resposta:\n" +
                "\"\n" +
                "[Whatsapp-Principal_Menu, Whatsapp-Menu_Configuracoes, Whatsapp-Configuracoes_Perfil]\n" +
                "\n" +
                "- Clique no botão de Menu da tela Principal (0)\n" +
                "- Clique na opção Configurações no Menu e depois clique na opção Perfil na tela de Configurações (1, 2)\n" +
                "\"";

        return new ChatGPTRequest(
                "gpt-3.5-turbo",
                List.of(
                        Map.of(
                                "role", "system",
                                "content", context
                        ),
                        Map.of(
                                "role", "user",
                                "content", exampleQuestion
                        ),
                        Map.of(
                                "role", "assistant",
                                "content", exampleAnswer
                        ),
                        Map.of(
                                "role", "user",
                                "content", question
                        )
                ),
                0.0,
                1000,
                0.0
        );

    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record ChatGPTRequest(String model, List<Map<String, String>> messages, Double temperature, Integer max_tokens, Double top_p) {
}
