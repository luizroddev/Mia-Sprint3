package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.Step;
import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.services.StepService;
import br.com.fiap.MiaDBD.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("steps")
public class StepController {

    private final StepService stepService;
    private final TaskService taskService;

    public StepController(StepService stepService, TaskService taskService) {
        this.stepService = stepService;
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Step>> getAllSteps() {
        List<Step> steps = stepService.getAllSteps();
        return new ResponseEntity<>(steps, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Step> getStepById(@PathVariable Integer id) {
        Step step = stepService.getStepById(id);
        if (step == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(step, HttpStatus.OK);
        }
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<Step>> getStepsByTaskId(@PathVariable Integer id) {
        List<Step> steps = stepService.getStepsByTaskId(id);
        if (steps == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(steps, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Step> createStep(@RequestBody Step step) {
        stepService.createStep(step);

        Task task = taskService.getTaskById(step.getTask().getId());
        task.getSteps().add(step);

        taskService.updateTask(task.getId(), task);
        return new ResponseEntity<>(step, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStep(@PathVariable Integer id, @RequestBody @Validated Step step) {
        stepService.updateStep(id, step);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable Integer id) {
        stepService.deleteStep(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
