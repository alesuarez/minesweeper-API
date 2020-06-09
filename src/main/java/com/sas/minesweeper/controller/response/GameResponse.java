package com.sas.minesweeper.controller.response;

import com.sas.minesweeper.util.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    private Long gameId;
    private GameStatus gameStatus;
    private LocalDateTime createdDate;
    private Long timeSpend;
    private Integer[][] board;
}
