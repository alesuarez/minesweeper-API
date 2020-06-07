package com.sas.minesweeper.controller.response;

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
    private Boolean isGameOver;
    private LocalDateTime createdDate;
    private Integer timeSpend;
    private Integer[][] board;
}
