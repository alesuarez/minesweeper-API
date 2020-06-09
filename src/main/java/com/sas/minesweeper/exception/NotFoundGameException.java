package com.sas.minesweeper.exception;

public class NotFoundGameException extends MinesweeperException{
    public NotFoundGameException() {
        super("The game is not valid");
    }
}
