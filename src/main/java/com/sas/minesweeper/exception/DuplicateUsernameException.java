package com.sas.minesweeper.exception;

public class DuplicateUsernameException extends MinesweeperException {
    public DuplicateUsernameException() {
        super("The username exist, please choose an other one");
    }
}
