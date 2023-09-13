package br.com.fiap.MiaDBD.records;

import java.util.List;
import java.util.Map;

public record MiaStepsResponse(String appName, String question, Map<String, List<String>> steps, List<String> screens, List<String> elements) {
}
