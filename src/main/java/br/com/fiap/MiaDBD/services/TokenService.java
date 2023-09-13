package br.com.fiap.MiaDBD.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.records.Credencial;
import br.com.fiap.MiaDBD.records.Token;
import br.com.fiap.MiaDBD.repositories.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenService {

    @Autowired
    UserRepository usuarioRepository;

    public Token generateToken(Credencial credencial) {
        Algorithm alg = Algorithm.HMAC256("meusecret");
        var jwt = JWT.create()
                .withSubject(credencial.email())
                .withIssuer("Mia")
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .sign(alg);
        return new Token(jwt, "JWT", "Bearer");
    }

    public User validate(String token) {
        Algorithm alg = Algorithm.HMAC256("meusecret");
        var email = JWT.require(alg)
                .withIssuer("Mia")
                .build()
                .verify(token)
                .getSubject();

        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new JWTVerificationException("Usuário nao encontrado"));

    }

}