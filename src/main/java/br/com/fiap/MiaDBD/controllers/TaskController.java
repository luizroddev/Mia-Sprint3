package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.Application;
import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.services.ApplicationService;
import br.com.fiap.MiaDBD.services.TaskService;
import br.com.fiap.MiaDBD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final UserService userService;

    private final ApplicationService applicationService;


    public TaskController(TaskService taskService, UserService userService, ApplicationService applicationService) {
        this.taskService = taskService;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
            List<Task> tasks = taskService.getAllTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        User user = userService.getUserById(task.getUser().getId());

        // Verifique se o usuário existe
        if (user == null) {
            // Trate o caso em que o usuário não existe, você pode retornar um ResponseEntity com HttpStatus.NOT_FOUND
            System.out.println("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskService.createTask(task);

        // Em seguida, adicione a tarefa à lista do usuário
        List<Task> userTasks = user.getTasks();

        if (userTasks == null) {
            userTasks = new ArrayList<>();
        }
        userTasks.add(task);
        user.setTasks(userTasks);

        userService.updateUser(user.getId(), user);

        // Retorne a tarefa criada com HttpStatus.CREATED
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Integer id, @RequestBody @Validated Task task) {
        taskService.updateTask(id, task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
