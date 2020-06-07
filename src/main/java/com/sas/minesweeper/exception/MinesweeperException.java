package com.sas.minesweeper.exception;

public class MinesweeperException extends RuntimeException {
    public MinesweeperException() {
        super();
    }

    public MinesweeperException(String message) {
        super(message);
    }
}
