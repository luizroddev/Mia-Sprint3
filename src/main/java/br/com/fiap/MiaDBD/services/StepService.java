package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.models.Step;
import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.repositories.StepRepository;
import br.com.fiap.MiaDBD.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StepService {

    private final StepRepository stepRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public StepService(StepRepository stepRepository, TaskRepository taskRepository) {
        this.stepRepository = stepRepository;
        this.taskRepository = taskRepository;
    }

    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    public Step getStepById(Integer id) {
        return stepRepository.findById(id).orElse(null);
    }

    public List<Step> getStepsByTaskId(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversa não encontrado com o ID: " + taskId));
        return task.getSteps();
    }

    public void createStep(Step step) {
        stepRepository.save(step);
    }

    public void updateStep(Integer id, Step step) {
        if (stepRepository.existsById(id)) {
            step.setId(id);
            stepRepository.save(step);
        }
    }

    public void deleteStep(Integer id) {
        stepRepository.deleteById(id);
    }
}