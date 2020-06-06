package com.sas.minesweeper.exception;

public class InvalidUserException extends MinesweeperException {
    public InvalidUserException() {
        super("The username or password are not valid");
    }
}
