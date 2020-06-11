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
public interface BoardMapper {

    @Mappings({
            @Mapping(source = "board", target = "board", qualifiedByName = "stringToIntegerArray"),
            @Mapping(source = "boardMasked", target = "maskedBoard", qualifiedByName = "stringToIntegerArray")
    })
    Board gameBoardToBoard(GameBoard gameBoard);

    @Named("stringToIntegerArray")
    static Integer[][] stringToIntegerArray(String board) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(board, Integer[][].class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
