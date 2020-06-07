package com.sas.minesweeper.entities;

import lombok.Data;

@Data
public class Board {
    private int rowsNumber;
    private int columnsNumber;
    private int bombsNumber;
    private Integer[][] board;
    private Integer[][] maskedBoard;

    public Board(int rowsNumber, int columnsNumber, int mines) {
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
        this.bombsNumber = mines;
        this.board = createEmptyBoard(0);
        this.maskedBoard = createEmptyBoard(-2);
    }

    private Integer[][] createEmptyBoard(int value) {
        Integer[][] table = new Integer[rowsNumber][columnsNumber];
        for (int i = 0; i < this.getRowsNumber(); i++) {
            for (int j = 0; j < this.getColumnsNumber(); j++) {
                table[i][j] = value;
            }
        }
        return table;
    }
}
