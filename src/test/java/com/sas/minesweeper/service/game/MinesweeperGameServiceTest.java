package com.sas.minesweeper.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.mapper.BoardGameMapper;
import com.sas.minesweeper.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MinesweeperGameServiceTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private BoardGameMapper boardGameMapperMock;

    @InjectMocks
    private MinesweeperGameService minesweeperGameService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void newGame_successful() {
        GameBoard gameBoard = GameBoard.builder().build();
        doReturn(gameBoard).when(boardGameMapperMock).boardToGameBoard(any(Board.class));

        NewGameRequest newGameRequest = NewGameRequest.builder()
                .column(3)
                .row(3)
                .bombs(1)
                .build();

        final GameResponse gameResponse = minesweeperGameService.newGame("usernameTest", newGameRequest);

        assertFalse(gameResponse.getIsGameOver());
        assertEquals(0, gameResponse.getTimeSpend());
        assertNotNull(gameResponse.getBoard());
        assertNotNull(gameResponse.getCreatedDate());

        verify(boardGameMapperMock, times(1)).boardToGameBoard(any(Board.class));
        verify(userServiceMock, times(1)).saveNewGame("usernameTest", gameBoard);
    }
}