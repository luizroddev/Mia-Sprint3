package br.com.fiap.MiaDBD.controllers;

import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.records.Credencial;
import br.com.fiap.MiaDBD.records.Token;
import br.com.fiap.MiaDBD.records.UsuarioLogin;
import br.com.fiap.MiaDBD.services.MiaChatGPTService;
import br.com.fiap.MiaDBD.services.TokenService;
import br.com.fiap.MiaDBD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class MiaChatGPTController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    private MiaChatGPTService service;

    @PostMapping("explanation")
    public Mono<String> createTaskExplanation(@RequestBody String question) {
        return service.createTaskExplanation(question).map(response -> response.choices().get(0).message().content());
    }

    @PostMapping("login")
    public ResponseEntity<UsuarioLogin> accessUser(@RequestBody Credencial credencial){
        manager.authenticate(credencial.toAuthentication());
        Token token = tokenService.generateToken(credencial);
        User usuario = userService.getUserByEmail((credencial.email()));
        UsuarioLogin usuarioLogin = new UsuarioLogin(usuario.getId(), usuario.getEmail(), usuario.getName(), token.token());
        return ResponseEntity.ok(usuarioLogin);
    }
}
