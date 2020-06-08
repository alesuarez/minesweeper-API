package com.sas.minesweeper.entities;

import static com.sas.minesweeper.util.CellContent.BOMB;
import static com.sas.minesweeper.util.CellContent.MASKED_CELL;
import static com.sas.minesweeper.util.CellContent.NOTHING;

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
        this.board = createEmptyBoard(NOTHING);
        this.maskedBoard = createEmptyBoard(MASKED_CELL);
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

    public void unMask(int row, int column) {
        this.maskedBoard[row][column] = this.board[row][column];
    }
    public void increaseCellValue(int row, int column) {
        this.board[row][column]++;
    }
    public void putMine(int row, int column) {
        this.board[row][column] = BOMB;
    }
    public int getCell(int row, int column) {
        return this.board[row][column];
    }
    public int getMaskedCell(int row, int column) {
        return this.maskedBoard[row][column];
    }
}