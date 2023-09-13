package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.records.Credencial;
import br.com.fiap.MiaDBD.records.Token;
import br.com.fiap.MiaDBD.records.UsuarioLogin;
import br.com.fiap.MiaDBD.services.TokenService;
import br.com.fiap.MiaDBD.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;


@RestController()
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/check/{token}")
    public ResponseEntity<User> checkTokenExpiration(@PathVariable String token) {
        User user = tokenService.validate(token);

        if (user.isCredentialsNonExpired()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioLogin> accessUser(@RequestBody Credencial credencial){
        manager.authenticate(credencial.toAuthentication());
        Token token = tokenService.generateToken(credencial);
        User usuario = userService.getUserByEmail((credencial.email()));
        UsuarioLogin usuarioLogin = new UsuarioLogin(usuario.getId(), usuario.getEmail(), usuario.getName(), token.token());
        return ResponseEntity.ok(usuarioLogin);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Validated User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Validated User user) {
        userService.updateUser(id, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
