package com.sas.minesweeper.repository;

import com.sas.minesweeper.model.MinesweeperUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinesweeperUserRepository extends CrudRepository<MinesweeperUser, Long>  {
    Optional<MinesweeperUser> findByUsername(String username);
}
