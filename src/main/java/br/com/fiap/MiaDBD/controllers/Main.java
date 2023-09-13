package br.com.fiap.MiaDBD.controllers;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String texto = "- Clique no botão de Menu da tela Principal (0)\n" +
                "- Clique na opção Configurações no Menu e depois clique na opção Perfil na tela de Configurações (1, 2)\n";

        String[] array = {"Whatsapp-Principal_Menu", "Whatsapp-Menu_Configuracoes", "Whatsapp-Configuracoes_Perfil"};

        List<String> arrayList = Arrays.asList(array);

        Map<String, List<String>> map = extractSteps(texto, arrayList);

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Chave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }
    }

    public static Map<String, List<String>> extractSteps(String text, List<String> arrayList) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("- (.*?) \\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);

        matcher.results().forEach(result -> {
            String sentence = result.group(1);
            List<String> values = Arrays.stream(result.group(2).split(", "))
                    .mapToInt(Integer::parseInt)
                    .filter(index -> index < arrayList.size())
                    .mapToObj(arrayList::get)
                    .collect(Collectors.toList());

            map.put(sentence, values);
        });

        return map;
    }
}
