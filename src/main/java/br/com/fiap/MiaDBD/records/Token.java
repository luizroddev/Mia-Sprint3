package br.com.fiap.MiaDBD.records;

public record Token(
        String token,
        String type,
        String prefix
) {}