package com.sas.minesweeper.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.minesweeper.entities.dtos.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper
public interface BoardGameMapper {

    @Mappings({
            @Mapping(source = "board", target = "board", qualifiedByName = "integerArrayToString"),
            @Mapping(source = "maskedBoard", target = "boardMasked", qualifiedByName = "integerArrayToString")
    })
    GameBoard boardToGameBoard(Board board);

    @Named("integerArrayToString")
    static String integerArrayToString(Integer[][] board) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
