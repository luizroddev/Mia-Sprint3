package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class MiaService {

    @Autowired
    private MiaFigmaService figmaService;

    @Autowired
    private MiaChatGPTService chatGPTService;

    @Autowired
    private ApplicationService applicationService;

    public static Map<String, List<String>> extractSteps(String text, List<String> arrayList) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("- (.*?) \\((.*?)\\)");
        Matcher matcher = pattern.matcher(text);

        matcher.results().forEach(result -> {
            String sentence = result.group(1);
            List<String> values = Arrays.stream(result.group(2).split(", ")).mapToInt(Integer::parseInt).filter(index -> index < arrayList.size()).mapToObj(arrayList::get).collect(Collectors.toList());

            map.put(sentence, values);
        });

        return map;
    }

    public Mono<MiaStepsResponse> getSteps(String appName, String question) {
        Mono<ChatGPTResponse> explanation = chatGPTService.createTaskExplanation(question);

        return explanation.map(choice -> {
            String content = choice.choices().get(0).message().content();

            List<String> steps = new ArrayList<>();

            try {
                String arrayString = content.substring(content.indexOf("["), content.indexOf("]") + 1);
                String arrayStringFormatted = arrayString.substring(1, arrayString.length() - 1);
                String[] array = arrayStringFormatted.split(", ");

                // Cria uma stream com as substrings e remove os colchetes individuais de cada uma
                steps.addAll(Arrays.stream(array).map(s -> s.replaceAll("\\[|\\]", "")).toList());
            } catch (Exception e) {
                System.out.println("ChatGPT falhou em criar a lista\n" + e);
            }

            Map<String, List<String>> map = extractSteps(choice.choices().get(0).message().content(), steps);

            return new MiaStepsResponse(appName, question, map, steps, steps);
        });
    }

    public Mono<MiaImagesResponse> getImages(MiaStepsResponse response) {
        String fileId = applicationService.getApplicationByName(response.appName().toLowerCase()).getFigmaId();

        return figmaService.getFigmaFileNodesIds(fileId).flatMap(figmaFile -> {
            DocumentScreen[] documentScreens = figmaFile.document().children()[0].children();
            List<DocumentScreen> screens = Arrays.stream(documentScreens).filter(screen -> response.screens().contains(screen.name())).toList();
            List<String> screenIds = screens.stream().map(DocumentScreen::id).collect(Collectors.toList());

            Mono<FigmaImagesResponse> screenImages = figmaService.getFigmaImage(fileId, String.join(",", screenIds));

            return screenImages.map(MiaImagesResponse::new);
        }).onErrorResume(e -> Mono.empty());
    }

}
