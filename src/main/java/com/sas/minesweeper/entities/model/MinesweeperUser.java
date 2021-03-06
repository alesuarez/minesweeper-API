package com.sas.minesweeper.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinesweeperUser {
    @Id
    @TableGenerator(name = "tab", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
    private long id;
    private String username;
    private String password;

    @OneToMany(mappedBy="minesweeperUser")
    private List<GameBoard> gameBoards;
}
