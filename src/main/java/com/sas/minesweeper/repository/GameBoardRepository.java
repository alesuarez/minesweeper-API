package com.sas.minesweeper.repository;

import com.sas.minesweeper.entities.model.GameBoard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GameBoardRepository extends CrudRepository<GameBoard, Long> {
    Optional<GameBoard> findByIdAndMinesweeperUser_Username(Long gameId, String username);
    List<GameBoard> findByMinesweeperUser_Username(String username);
}
