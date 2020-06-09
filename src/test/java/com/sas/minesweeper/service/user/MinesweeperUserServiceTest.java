package com.sas.minesweeper.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class MinesweeperUserServiceTest {

    @Mock
    private MinesweeperUserRepository minesweeperUserRepositoryMock;

    @InjectMocks
    private MinesweeperUserService minesweeperUserService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveNewGame_shouldReturnSuccessful() {
        MinesweeperUser minesweeperUser = MinesweeperUser.builder().build();
        doReturn(Optional.of(minesweeperUser)).when(minesweeperUserRepositoryMock).findByUsername("usernameTest");

        final MinesweeperUser user = minesweeperUserService.getUser("usernameTest");

        assertEquals(minesweeperUser, user);
        verify(minesweeperUserRepositoryMock, times(1)).findByUsername("usernameTest");
    }

    @Test
    void saveNewGame_shouldThrowInvalidUserException() {

        doReturn(Optional.empty()).when(minesweeperUserRepositoryMock).findByUsername("usernameTest");

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            minesweeperUserService.getUser("usernameTest");
        });

        assertEquals("Error user not found", exception.getMessage());
    }
}