package com.sas.minesweeper.service.game.strategy;

import static com.sas.minesweeper.util.GameStatus.GAME_OVER;
import static com.sas.minesweeper.util.GameStatus.PLAYING;
import static com.sas.minesweeper.util.GameStatus.WIN;
import static com.sas.minesweeper.utils.TestUtils.assertEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.util.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class DiscoveryStrategyTest {

    @InjectMocks
    private DiscoveryStrategy discoveryStrategy;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_shouldReturnPlaying() {

        Board board = buildBoard();

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .column(5)
                .row(0)
                .build();

        GameStatus gameStatus = discoveryStrategy.execute(board, updateGameRequest);

        assertEquals(PLAYING, gameStatus);
        assertEqual(expectedMaskWhenIsPlaying(), board.getMaskedBoard(), 6);
    }

    @Test
    public void execute_shouldReturnGameOver() {

        Board board = buildBoard();

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .column(1)
                .row(1)
                .build();

        GameStatus gameStatus = discoveryStrategy.execute(board, updateGameRequest);

        assertEquals(GAME_OVER, gameStatus);
        assertEqual(expectedMaskWhenIsGameOver(), board.getMaskedBoard(), 6);
    }

    @Test
    public void execute_shouldReturnWin() {

        Integer[][] boardInteger = {{0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 1, -1, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}};

        Integer[][] maskedBoardInteger = {{-2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2}};

        Board board = Board.builder()
                .board(boardInteger)
                .maskedBoard(maskedBoardInteger)
                .bombsNumber(1)
                .columnsNumber(5)
                .rowsNumber(5)
                .build();

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .column(0)
                .row(0)
                .build();

        GameStatus gameStatus = discoveryStrategy.execute(board, updateGameRequest);

        assertEquals(WIN, gameStatus);
        assertEqual(expectedMaskWhenIsWin(), board.getMaskedBoard(), 5);
    }

    private Board buildBoard() {
        Integer[][] boardInteger = {{1, 1, 1, 0, 0, 0},
                {1, -1, 1, 0, 0, 0},
                {1, 1, 2, 1, 1, 0},
                {0, 0, 1, -1, 1, 0},
                {0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0}};

        Integer[][] maskedBoardInteger = {{-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2}};

        return Board.builder()
                .board(boardInteger)
                .maskedBoard(maskedBoardInteger)
                .bombsNumber(2)
                .columnsNumber(6)
                .rowsNumber(6)
                .build();
    }

    private Integer[][] expectedMaskWhenIsPlaying() {
        return new Integer[][]{{-2, -2, 1, 0, 0, 0},
                {-2, -2, 1, 0, 0, 0},
                {1, 1, 2, 1, 1, 0},
                {0, 0, 1, -2, 1, 0},
                {0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0}};
    }

    private Integer[][] expectedMaskWhenIsGameOver() {
        return new Integer[][]{{-2, -2, -2, -2, -2, -2},
                {-2, -1, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -1, -2, -2},
                {-2, -2, -2, -2, -2, -2},
                {-2, -2, -2, -2, -2, -2}};
    }

    private Integer[][] expectedMaskWhenIsWin() {
        return new Integer[][]{{0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 1, -2, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}};
    }
}