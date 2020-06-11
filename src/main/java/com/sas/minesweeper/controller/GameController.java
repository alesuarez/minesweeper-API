package com.sas.minesweeper.controller;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.service.GameService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@CrossOrigin("https://quiet-dusk-07692.herokuapp.com")
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

    @PutMapping
    @ApiOperation(value = "Update a game", tags = "game")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", dataType = "string", paramType = "header")
    public ResponseEntity<GameResponse> putGame(
            @RequestBody @Validated UpdateGameRequest updateGameRequest, Principal principal) {
        return ResponseEntity.ok(gameService.updateGame(principal.getName(), updateGameRequest));
    }

    @GetMapping
    @ApiOperation(value = "Get all games", tags = "game")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", dataType = "string", paramType = "header")
    public ResponseEntity<List<GameResponse>> getGames(Principal principal) {
        return ResponseEntity.ok(gameService.getAll(principal.getName()));
    }

    @GetMapping(path = "/{gameId}}")
    @ApiOperation(value = "Get a game", tags = "game")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", dataType = "string", paramType = "header")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long gameId, Principal principal) {
        return ResponseEntity.ok(gameService.getGame(principal.getName(), gameId));
    }
}
