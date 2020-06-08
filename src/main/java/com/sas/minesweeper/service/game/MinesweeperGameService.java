package com.sas.minesweeper.service.game;

import static com.sas.minesweeper.util.CellContent.BOMB;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.mapper.BoardGameMapper;
import com.sas.minesweeper.service.GameService;
import com.sas.minesweeper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperGameService implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(MinesweeperGameService.class);

    private static final int ZERO_INDEX = 0;
    private static final int ONE_INDEX = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardGameMapper boardGameMapper;

    @Override
    public GameResponse newGame(String username, NewGameRequest newGameRequest) {
        logger.info("Creating a new game rows: {} columns: {} bombs: {} for user {}", newGameRequest.getRow(), newGameRequest.getColumn(), newGameRequest.getBombs(), username);

        Board board = new Board(newGameRequest.getRow(), newGameRequest.getColumn(), newGameRequest.getBombs());
        putBombs(board);

        GameBoard gameBoard = boardGameMapper.boardToGameBoard(board);
        gameBoard.newGameBoard();

        userService.saveNewGame(username, gameBoard);

        logger.info("Creating a new game for user {} successful", username);

        return GameResponse.builder()
                .createdDate(gameBoard.getCreatedDate())
                .isGameOver(gameBoard.isGameOver())
                .timeSpend(0)
                .board(board.getMaskedBoard())
                .build();
    }

    private void putBombs(Board board) {
        for (int i = ZERO_INDEX; i < board.getBombsNumber();) {
            int randomColumn = random(board.getColumnsNumber());
            int randomRow = random(board.getRowsNumber());

            if (board.getCell(randomRow, randomColumn) != BOMB) {
                board.putMine(randomRow, randomColumn);
                fillNeighborhood(board, randomRow, randomColumn);
                i++;
            }
        }
    }

    private void fillNeighborhood(Board board, int randomRow, int randomColumn) {
        int minColumn = getMin(randomColumn);
        int maxColumn = getMax(randomColumn, board.getColumnsNumber());
        int minRow = getMin(randomRow);
        int maxRow = getMax(randomRow, board.getRowsNumber());

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                if (board.getCell(i, j) != BOMB) {
                    board.increaseCellValue(i, j);
                }
            }
        }
    }

    private int random(int columnsNumber) {
        return (int) (Math.random() * columnsNumber);
    }
    private int getMin(int number) {
        return number == ZERO_INDEX ? number : number - ONE_INDEX;
    }
    private int getMax(int number, int max) {
        return number + ONE_INDEX >= max ? number : number + ONE_INDEX;
    }
}
