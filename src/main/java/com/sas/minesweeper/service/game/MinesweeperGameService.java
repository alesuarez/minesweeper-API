package com.sas.minesweeper.service.game;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.mapper.BoardGameMapper;
import com.sas.minesweeper.service.GameService;
import com.sas.minesweeper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperGameService implements GameService {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardGameMapper boardGameMapper;

    @Override
    public GameResponse newGame(String username, NewGameRequest newGameRequest) {
        Board board = new Board(newGameRequest.getRow(), newGameRequest.getColumn(), newGameRequest.getBombs());

        GameBoard gameBoard = boardGameMapper.boardToGameBoard(board);
        gameBoard.newGameBoard();

        userService.saveNewGame(username, gameBoard);

        return GameResponse.builder()
                .createdDate(gameBoard.getCreatedDate())
                .isGameOver(gameBoard.isGameOver())
                .timeSpend(0)
                .board(board.getMaskedBoard())
                .build();
    }
}
