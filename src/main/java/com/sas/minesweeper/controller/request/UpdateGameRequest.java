package com.sas.minesweeper.controller.request;

import com.sas.minesweeper.util.ShootType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGameRequest {
    private Long gameId;
    private int row;
    private int column;
    private ShootType shootType;
}
