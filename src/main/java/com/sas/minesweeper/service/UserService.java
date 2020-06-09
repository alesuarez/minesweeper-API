package com.sas.minesweeper.service;

import com.sas.minesweeper.entities.model.MinesweeperUser;

public interface UserService {
    MinesweeperUser getUser(String username);
}
