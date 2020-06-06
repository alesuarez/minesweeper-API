package com.sas.minesweeper.service;

import com.sas.minesweeper.controller.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username, String password);
    AuthResponse register(String username, String password);
}
