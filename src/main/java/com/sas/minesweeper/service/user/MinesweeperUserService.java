package com.sas.minesweeper.service.user;

import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.repository.GameBoardRepository;
import com.sas.minesweeper.repository.MinesweeperUserRepository;
import com.sas.minesweeper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinesweeperUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(MinesweeperUserService.class);

    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private MinesweeperUserRepository minesweeperUserRepository;

    @Override
    public void saveNewGame(String username, GameBoard board) {
        logger.info("Saving a new game for user {}", username);

        MinesweeperUser user = getUser(username);
        board.setMinesweeperUser(user);
        gameBoardRepository.save(board);

        logger.info("Saving a new game for user {} successful", username);
    }

    private MinesweeperUser getUser(String username) {
        return minesweeperUserRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("Error user {} not found", username);
            throw new InvalidUserException();
        });
    }
}
