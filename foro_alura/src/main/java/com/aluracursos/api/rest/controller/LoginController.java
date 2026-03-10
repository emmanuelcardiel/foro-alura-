package com.aluracursos.api.rest.controller;


import com.aluracursos.api.rest.dto.LoginDTO;
import com.aluracursos.api.rest.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public String login(@RequestBody @Valid LoginDTO login) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.username(), login.password())
        );

        String token = tokenService.generarToken(authentication.getName());
        return token;
    }
}
