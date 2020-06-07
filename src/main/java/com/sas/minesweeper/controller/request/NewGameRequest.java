package com.sas.minesweeper.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewGameRequest {
    private int bombs;
    private int row;
    private int column;
}
