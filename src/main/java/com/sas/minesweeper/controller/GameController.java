package com.sas.minesweeper.controller;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.service.GameService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    @ApiOperation(value = "Create a new game", tags = "game")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", dataType = "string", paramType = "header")
    public ResponseEntity<GameResponse> postNewGame(
            @RequestBody @Validated NewGameRequest newGameRequest, Principal principal) {
        return ResponseEntity.ok(gameService.newGame(principal.getName(), newGameRequest));
    }
}
