package com.sas.minesweeper.service;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;

public interface GameService {
    GameResponse newGame(String username, NewGameRequest newGameRequest);
}
