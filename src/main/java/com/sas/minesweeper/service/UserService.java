package com.sas.minesweeper.service;

import com.sas.minesweeper.entities.model.GameBoard;

public interface UserService {
    void saveNewGame(String username, GameBoard gameBoard);
}
