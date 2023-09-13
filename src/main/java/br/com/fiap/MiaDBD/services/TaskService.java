package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.repositories.TaskRepository;
import br.com.fiap.MiaDBD.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID: " + userId));
        return user.getTasks();
    }

    public void createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public void updateTask(Integer id, Task task) {
        Task existingTask = taskRepository.findById(id).orElse(null);

        if (existingTask != null) {
            task.setId(id);
            task.setCreatedAt(existingTask.getCreatedAt());
            taskRepository.save(task);
        }
    }

    public void deleteTask(Integer id) {
        taskRepository.findById(id).ifPresent(taskRepository::delete);
    }
}
