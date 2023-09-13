package br.com.fiap.MiaDBD.config;


import br.com.fiap.MiaDBD.models.User;
import br.com.fiap.MiaDBD.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // pegar o token do header
        String token = getToken(request);
        System.out.println(token);

        // se tiver um token
        if (token != null){
            //validar
            User usuario = tokenService.validate(token);

            // autenticar
            Authentication auth = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, usuario.getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization"); // Bearer eyhjshdkjhfks.gusjdfkgjsdfkjg.ndkfsdkfgbksd

        if (header == null || !header.startsWith("Bearer ")){
            return null;
        }

        return header.replace("Bearer ", "");
    }

}