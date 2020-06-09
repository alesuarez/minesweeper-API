package com.sas.minesweeper.service.game;

import static com.sas.minesweeper.util.GameStatus.GAME_OVER;
import static com.sas.minesweeper.util.GameStatus.PLAYING;
import static com.sas.minesweeper.util.ShootType.DISCOVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.mapper.BoardGameMapper;
import com.sas.minesweeper.mapper.BoardMapper;
import com.sas.minesweeper.repository.GameBoardRepository;
import com.sas.minesweeper.service.UserService;
import com.sas.minesweeper.service.game.strategy.DiscoveryStrategy;
import com.sas.minesweeper.util.ShootType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

class MinesweeperGameServiceTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private BoardGameMapper boardGameMapperMock;

    @Mock
    private BoardMapper boardMapperMock;

    @Mock
    private GameBoardRepository gameBoardRepositoryMock;

    @Mock
    private Map<ShootType, PlayStrategy> strategyMapMock;

    @InjectMocks
    private MinesweeperGameService minesweeperGameService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void newGame_shouldReturnSuccessful() {

        GameBoard gameBoard = GameBoard.builder().build();
        doReturn(gameBoard).when(boardGameMapperMock).boardToGameBoard(any(Board.class));

        GameBoard gameBoardSaved = GameBoard.builder().id(1l).minesweeperUser(MinesweeperUser.builder().username("username").build()).build();
        doReturn(gameBoardSaved).when(gameBoardRepositoryMock).save(gameBoard);

        NewGameRequest newGameRequest = NewGameRequest.builder()
                .column(3)
                .row(3)
                .bombs(1)
                .build();

        final GameResponse gameResponse = minesweeperGameService.newGame("usernameTest", newGameRequest);

        assertEquals(PLAYING, gameResponse.getGameStatus());
        assertEquals(0, gameResponse.getTimeSpend());
        assertNotNull(gameResponse.getBoard());
        assertNotNull(gameResponse.getCreatedDate());

        verify(boardGameMapperMock, times(1)).boardToGameBoard(any(Board.class));
        verify(userServiceMock, times(1)).getUser("usernameTest");
        verify(gameBoardRepositoryMock, times(1)).save(gameBoard);
    }

    @Test
    public void updateGame_shouldContinuePlaying() {
        GameBoard gameBoard = GameBoard.builder()
                .gameStatus(PLAYING)
                .createdDate(LocalDateTime.now())
                .minesweeperUser(MinesweeperUser.builder().username("usernameTest").build())
                .build();
        doReturn(Optional.of(gameBoard)).when(gameBoardRepositoryMock).findByIdAndMinesweeperUser_Username(1L, "usernameTest");

        Integer[][] maskedInteger = {{-2,-2,-2,-2,-2,-2}};

        Board board = Board.builder().maskedBoard(maskedInteger).build();
        doReturn(board).when(boardMapperMock).gameBoardToBoard(gameBoard);

        PlayStrategy discoveryStrategy = mock(DiscoveryStrategy.class);
        doReturn(discoveryStrategy).when(strategyMapMock).get(DISCOVER);

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .gameId(1L)
                .column(1)
                .row(1)
                .shootType(DISCOVER)
                .build();

        doReturn(PLAYING).when(discoveryStrategy).execute(board, updateGameRequest);

        doReturn(gameBoard).when(boardGameMapperMock).boardToGameBoard(board);

        GameBoard gameBoardSaved = GameBoard.builder().id(1l).minesweeperUser(MinesweeperUser.builder().username("usernameTest").build()).build();
        doReturn(gameBoardSaved).when(gameBoardRepositoryMock).save(gameBoard);

        final GameResponse gameResponse = minesweeperGameService.updateGame("usernameTest", updateGameRequest);

        assertEquals(PLAYING, gameResponse.getGameStatus());
        assertNotNull(gameResponse.getTimeSpend());
        assertNotNull(gameResponse.getBoard());
        assertNotNull(gameResponse.getCreatedDate());

        verify(gameBoardRepositoryMock, times(1)).findByIdAndMinesweeperUser_Username(1L, "usernameTest");
        verify(boardMapperMock, times(1)).gameBoardToBoard(gameBoard);
        verify(strategyMapMock, times(1)).get(DISCOVER);
        verify(discoveryStrategy, times(1)).execute(board, updateGameRequest);
        verify(boardGameMapperMock, times(1)).boardToGameBoard(board);
        verify(gameBoardRepositoryMock, times(1)).save(gameBoard);
    }

    @Test
    public void updateGame_shouldReturnGameOver() {
        GameBoard gameBoard = GameBoard.builder()
                .gameStatus(GAME_OVER)
                .createdDate(LocalDateTime.now())
                .lastTimeUpdated(LocalDateTime.now().plusSeconds(1))
                .minesweeperUser(MinesweeperUser.builder().username("usernameTest").build())
                .build();

        doReturn(Optional.of(gameBoard)).when(gameBoardRepositoryMock).findByIdAndMinesweeperUser_Username(1L, "usernameTest");

        Integer[][] maskedInteger = {{-2,-2,-2,-2,-2,-2}};

        Board board = Board.builder().maskedBoard(maskedInteger).build();
        doReturn(board).when(boardMapperMock).gameBoardToBoard(gameBoard);

        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .gameId(1L)
                .column(1)
                .row(1)
                .shootType(DISCOVER)
                .build();


        doReturn(gameBoard).when(boardGameMapperMock).boardToGameBoard(board);

        final GameResponse gameResponse = minesweeperGameService.updateGame("usernameTest", updateGameRequest);

        assertEquals(GAME_OVER, gameResponse.getGameStatus());
        assertEquals(1, gameResponse.getTimeSpend());
        assertNotNull(gameResponse.getBoard());
        assertNotNull(gameResponse.getCreatedDate());

        verify(gameBoardRepositoryMock, times(1)).findByIdAndMinesweeperUser_Username(1L, "usernameTest");
        verify(boardMapperMock, times(1)).gameBoardToBoard(gameBoard);
        verify(strategyMapMock, times(0)).get(any());
        verify(boardGameMapperMock, times(0)).boardToGameBoard(any());
        verify(gameBoardRepositoryMock, times(0)).save(any());
    }
}