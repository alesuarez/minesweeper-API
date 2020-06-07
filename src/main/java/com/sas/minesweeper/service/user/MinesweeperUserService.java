package com.sas.minesweeper.service.user;

import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.GameBoardRepository;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import com.sas.minesweeper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperUserService implements UserService {

    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private MinesweeperUserRepository minesweeperUserRepository;

    @Override
    public void saveNewGame(String username, GameBoard board) {
        MinesweeperUser user = getUser(username);
        board.setMinesweeperUser(user);
        gameBoardRepository.save(board);
    }

    private MinesweeperUser getUser(String username) {
        return minesweeperUserRepository.findByUsername(username).orElseThrow(InvalidUserException::new);
    }
}
