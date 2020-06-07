package com.sas.minesweeper.repository;

import com.sas.minesweeper.entities.model.GameBoard;
import org.springframework.data.repository.CrudRepository;

public interface GameBoardRepository extends CrudRepository<GameBoard, Long> {
}
