package com.sas.minesweeper.controller;

import com.sas.minesweeper.controller.response.AuthResponse;
import com.sas.minesweeper.entities.OAuthMinesweeper;
import com.sas.minesweeper.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    @ApiOperation(value = "Login a client", tags = "auth")
    public ResponseEntity<AuthResponse> postLogin(
            @RequestBody @Validated OAuthMinesweeper authLoginRequest) {

        AuthResponse response =
                authService.login(authLoginRequest.getUsername(), authLoginRequest.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register")
    @ApiOperation(value = "Register a client", tags = "auth")
    public ResponseEntity<AuthResponse> postRegister(
            @RequestBody @Validated OAuthMinesweeper authLoginRequest) {

        AuthResponse response =
                authService.register(authLoginRequest.getUsername(), authLoginRequest.getPassword());

        return ResponseEntity.ok(response);
    }
}
