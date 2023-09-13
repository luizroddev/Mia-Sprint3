package br.com.fiap.MiaDBD.records;

import java.util.Map;
import java.util.Objects;

public record FigmaImagesResponse(String err, Map<String, String> images) {
    public FigmaImagesResponse {
        Objects.requireNonNull(images, "images must not be null");
    }
}
