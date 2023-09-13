package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.records.FigmaFileResponse;
import br.com.fiap.MiaDBD.records.FigmaImagesResponse;
import br.com.fiap.MiaDBD.services.ApplicationService;
import br.com.fiap.MiaDBD.services.MiaFigmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class MiaFigmaController {

    @Autowired
    private MiaFigmaService service;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("getFigmaImage/{fileId}/{nodeId}")
    public Mono<FigmaImagesResponse> getFigmaImage(@PathVariable String fileId, @PathVariable String nodeId) {
        return service.getFigmaImage(fileId, nodeId);
    }

    @GetMapping("getFigmaFileNodes/{appName}")
    public Mono<FigmaFileResponse> getFigmaFileNodesIds(@PathVariable String appName) {
        String fileId = applicationService.getApplicationByName(appName.toLowerCase()).getFigmaId();
        System.out.println(fileId);

        return service.getFigmaFileNodesIds(fileId);
    }
}
