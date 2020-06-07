package com.sas.minesweeper.service.user;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.repository.GameBoardRepository;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class MinesweeperUserServiceTest {

    @Mock
    private GameBoardRepository gameBoardRepositoryMock;

    @Mock
    private MinesweeperUserRepository minesweeperUserRepositoryMock;

    @InjectMocks MinesweeperUserService minesweeperUserService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveNewGame_successful() {
        MinesweeperUser minesweeperUser = MinesweeperUser.builder().build();

        doReturn(Optional.of(minesweeperUser)).when(minesweeperUserRepositoryMock).findByUsername("usernameTest");

        GameBoard gameBoard = GameBoard.builder()
                .build();

        minesweeperUserService.saveNewGame("usernameTest", gameBoard);

        verify(minesweeperUserRepositoryMock, times(1)).findByUsername("usernameTest");
        verify(gameBoardRepositoryMock, times(1)).save(gameBoard);
    }
}