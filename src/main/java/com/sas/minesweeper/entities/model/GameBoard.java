package com.sas.minesweeper.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameBoard {

    @Id
    @TableGenerator(name = "tab", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
    private long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastTimeUpdated;
    private boolean gameOver;
    private int rowsNumber;
    private int columnsNumber;
    private int bombsNumber;
    @Column(columnDefinition = "TEXT")
    private String board;
    @Column(columnDefinition = "TEXT")
    private String boardMasked;

    @ManyToOne
    @JoinColumn(name="minesweeper_user_id", nullable=false)
    private MinesweeperUser minesweeperUser;

    public void newGameBoard() {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.lastTimeUpdated = localDateTime;
        this.createdDate = localDateTime;
        this.gameOver = false;
    }
}
