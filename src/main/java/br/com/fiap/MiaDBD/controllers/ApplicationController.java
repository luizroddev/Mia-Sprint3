package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.Application;
import br.com.fiap.MiaDBD.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/app")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Integer id) {
        Application application = applicationService.getApplicationById(id);
        if (application == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(application, HttpStatus.OK);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Application> getApplicationByName(@PathVariable String name) {
        Application application = applicationService.getApplicationByName(name);
        if (application == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(application, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createApplication(@RequestBody @Validated Application application) {
        applicationService.createApplication(application);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateApplication(@PathVariable Integer id, @RequestBody @Validated Application application) {
        applicationService.updateApplication(id, application);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
