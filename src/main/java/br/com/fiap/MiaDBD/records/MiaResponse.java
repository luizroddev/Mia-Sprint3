package br.com.fiap.MiaDBD.records;

import br.com.fiap.MiaDBD.records.MiaImagesResponse;
import br.com.fiap.MiaDBD.records.MiaStepsResponse;

public record MiaResponse(MiaStepsResponse steps, MiaImagesResponse images) {
}
