package com.sas.minesweeper.service.game.strategy;

import static com.sas.minesweeper.util.GameStatus.PLAYING;
import static com.sas.minesweeper.utils.TestUtils.assertEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.util.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class FlaggedStrategyTest {

    @InjectMocks
    private FlaggedStrategy flaggedStrategy;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_shouldReturnPlaying() {
        Integer[][] boardInteger = {{0, 0}, {0, 0}};

        Integer[][] maskedBoardInteger = {{-2, -2}, {-2, -2}};

        Board board = Board.builder()
                .board(boardInteger)
                .maskedBoard(maskedBoardInteger)
                .bombsNumber(0)
                .columnsNumber(2)
                .rowsNumber(2)
                .build();

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .column(0)
                .row(0)
                .build();

        GameStatus gameStatus = flaggedStrategy.execute(board, updateGameRequest);

        assertEquals(PLAYING, gameStatus);
        assertEqual(expectedMaskWheNIsFlagged(), board.getMaskedBoard(), 2);
    }

    private Integer[][] expectedMaskWheNIsFlagged() {
        return new Integer[][]{{-3, -2}, {-2, -2}};
    }
}