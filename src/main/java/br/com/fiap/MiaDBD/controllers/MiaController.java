package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.Step;
import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.records.MiaResponse;
import br.com.fiap.MiaDBD.records.Question;
import br.com.fiap.MiaDBD.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class MiaController {

    @Autowired
    private MiaService service;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StepService stepService;

    @PostMapping("ask")
    public Mono<ResponseEntity<MiaResponse>> getStepsAndImages(@RequestBody Question question) {
        Mono<MiaResponse> response = service.getSteps(question.getApp(), question.getText())
                .flatMap(stepsResponse -> service.getImages(stepsResponse)
                        .map(imagesResponse -> new MiaResponse(stepsResponse, imagesResponse)));

        return response
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
