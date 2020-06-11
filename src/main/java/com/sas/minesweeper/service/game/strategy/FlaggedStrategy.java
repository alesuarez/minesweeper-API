package com.sas.minesweeper.service.game.strategy;

import static com.sas.minesweeper.util.GameStatus.PLAYING;

import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.entities.dtos.Board;
import com.sas.minesweeper.service.game.PlayStrategy;
import com.sas.minesweeper.util.GameStatus;
import org.springframework.stereotype.Service;

@Service
public class FlaggedStrategy implements PlayStrategy {

    @Override
    public GameStatus execute(Board board, UpdateGameRequest updateGameRequest) {
        board.putFlag(updateGameRequest.getRow(), updateGameRequest.getColumn());
        return PLAYING;
    }
}
