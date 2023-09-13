package br.com.fiap.MiaDBD.records;

import java.util.List;

public record ChatGPTResponse(List<Choice> choices) {
}
