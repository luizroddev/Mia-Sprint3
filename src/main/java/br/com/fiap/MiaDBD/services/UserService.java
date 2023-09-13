package br.com.fiap.MiaDBD.services;

import br.com.fiap.MiaDBD.models.Task;
import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Task createTask() {

        return new Task();
    }

    public void createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);

        System.out.println("tentou dar update");
        if (existingUser != null) {
            user.setId(existingUser.getId());
            user.setCreatedAt(existingUser.getCreatedAt());
            System.out.println("entrou metodo update");
            System.out.println(user.getTasks().size() + " tamanho");
            userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            userRepository.delete(existingUser);
        }
    }
}
