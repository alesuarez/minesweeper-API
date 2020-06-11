package com.sas.minesweeper.service.game;

import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.entities.dtos.Board;
import com.sas.minesweeper.util.GameStatus;

public interface PlayStrategy {
    GameStatus execute(Board board, UpdateGameRequest updateGameRequest);
}
