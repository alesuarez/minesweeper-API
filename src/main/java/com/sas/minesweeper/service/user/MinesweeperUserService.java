package com.sas.minesweeper.service.user;

import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.InvalidUserException;
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
    private MinesweeperUserRepository minesweeperUserRepository;

    public MinesweeperUser getUser(String username) {
        return minesweeperUserRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("Error user {} not found", username);
            throw new InvalidUserException("Error user not found");
        });
    }
}
