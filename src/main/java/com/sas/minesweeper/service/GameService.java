package com.sas.minesweeper.service;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;

import java.util.List;

public interface GameService {
    GameResponse newGame(String username, NewGameRequest newGameRequest);
    GameResponse updateGame(String username, UpdateGameRequest updateGameRequest);
    List<GameResponse> getAll(String username);
    GameResponse getGame(String username, Long gameId);
}
