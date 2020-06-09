package com.sas.minesweeper.service.game.strategy;

import static com.sas.minesweeper.util.CellContent.BOMB;
import static com.sas.minesweeper.util.CellContent.FLAG;
import static com.sas.minesweeper.util.CellContent.MASK;
import static com.sas.minesweeper.util.CellContent.NOTHING;
import static com.sas.minesweeper.util.GameStatus.GAME_OVER;
import static com.sas.minesweeper.util.GameStatus.PLAYING;
import static com.sas.minesweeper.util.GameStatus.WIN;

import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.entities.Board;
import com.sas.minesweeper.service.game.PlayStrategy;
import com.sas.minesweeper.util.GameStatus;
import org.springframework.stereotype.Service;

@Service
public class DiscoveryStrategy implements PlayStrategy {

    @Override
    public GameStatus execute(Board board, UpdateGameRequest updateGameRequest) {
        if (unMaskNeighborhood(board, updateGameRequest.getRow(), updateGameRequest.getColumn())) {
            unMaskAllBombs(board);
            return GAME_OVER;
        }
        return isFoundAllBombs(board) ? WIN : PLAYING;
    }

    private void unMaskAllBombs(Board board) {
        for (int i = 0; i < board.getRowsNumber(); i++) {
            for (int j = 0; j < board.getColumnsNumber(); j++) {
                if (board.getCell(i, j) == BOMB) {
                    board.unMask(i, j);
                }
            }
        }
    }

    private boolean isFoundAllBombs(Board board) {
        int bombsFound = 0;
        int maskedCells = 0;
        for (int i = 0; i < board.getRowsNumber(); i++) {
            for (int j = 0; j < board.getColumnsNumber(); j++) {
                if (board.getMaskedCell(i, j) == MASK || board.getMaskedCell(i, j) == FLAG) {
                    if (board.getCell(i, j) == BOMB) {
                        bombsFound++;
                    }
                    maskedCells++;
                }
            }
        }
        return bombsFound == maskedCells;
    }

    private boolean unMaskNeighborhood(Board board, int row, int column) {
        boolean isIndexOk = row >= 0 && column >= 0 && row < board.getRowsNumber() && column < board.getColumnsNumber();

        if (isIndexOk && (board.getMaskedCell(row, column) == MASK || board.getMaskedCell(row, column) == FLAG)) {
            board.unMask(row, column);
            int cellContent = board.getCell(row, column);
            if (cellContent == BOMB) {
                return true;
            } else if (cellContent == NOTHING) {
                boolean unMaskUpNeighborhood = unMaskNeighborhood(board, row - 1, column - 1) || unMaskNeighborhood(board, row - 1, column) || unMaskNeighborhood(board, row - 1, column + 1);
                boolean unMaskCloseNeighborhood = unMaskNeighborhood(board, row, column - 1) || unMaskNeighborhood(board, row, column + 1);
                boolean unMaskDownNeighborhood = unMaskNeighborhood(board, row + 1, column - 1) || unMaskNeighborhood(board, row + 1, column) || unMaskNeighborhood(board, row + 1, column + 1);

                return unMaskUpNeighborhood || unMaskCloseNeighborhood || unMaskDownNeighborhood;
            }
        }
        return false;
    }
}
