package com.sas.minesweeper.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.model.GameBoard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper
public interface GameResponseMapper {
    @Mappings({
            @Mapping(source = "id", target = "gameId"),
            @Mapping(source = "boardMasked", target = "board", qualifiedByName = "stringToIntegerArray"),
            @Mapping(target = "timeSpend", expression="java(java.time.Duration.between(gameBoard.getCreatedDate(), gameBoard.getLastTimeUpdated()).getSeconds())")
    })
    GameResponse gameBoardToGameResponse(GameBoard gameBoard);

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
