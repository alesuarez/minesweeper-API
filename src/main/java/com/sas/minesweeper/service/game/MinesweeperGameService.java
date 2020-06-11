package com.sas.minesweeper.service.game;

import static com.sas.minesweeper.util.CellContent.BOMB;
import static com.sas.minesweeper.util.GameStatus.GAME_OVER;
import static com.sas.minesweeper.util.GameStatus.PLAYING;

import com.sas.minesweeper.controller.request.NewGameRequest;
import com.sas.minesweeper.controller.request.UpdateGameRequest;
import com.sas.minesweeper.controller.response.GameResponse;
import com.sas.minesweeper.entities.dtos.Board;
import com.sas.minesweeper.entities.model.GameBoard;
import com.sas.minesweeper.entities.model.MinesweeperUser;
import com.sas.minesweeper.exception.NotFoundGameException;
import com.sas.minesweeper.mapper.BoardGameMapper;
import com.sas.minesweeper.mapper.BoardMapper;
import com.sas.minesweeper.mapper.GameResponseMapper;
import com.sas.minesweeper.repository.GameBoardRepository;
import com.sas.minesweeper.service.GameService;
import com.sas.minesweeper.service.UserService;
import com.sas.minesweeper.util.GameStatus;
import com.sas.minesweeper.util.ShootType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MinesweeperGameService implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(MinesweeperGameService.class);

    private static final int ZERO_INDEX = 0;
    private static final int ONE_INDEX = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardGameMapper boardGameMapper;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private GameResponseMapper gameResponseMapper;

    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private Map<ShootType, PlayStrategy> strategyMap;


    @Override
    public GameResponse newGame(String username, NewGameRequest newGameRequest) {
        logger.info("Creating a new game rows: {} columns: {} bombs: {} for user {}", newGameRequest.getRow(), newGameRequest.getColumn(), newGameRequest.getBombs(), username);

        Board board = new Board(newGameRequest.getRow(), newGameRequest.getColumn(), newGameRequest.getBombs());
        putBombs(board);

        GameBoard gameBoard = boardGameMapper.boardToGameBoard(board);

        long gameBoardId = saveNewGameBoard(username, gameBoard).getId();

        logger.info("Creating a new game for user {} successful", username);

        return GameResponse.builder()
                .gameId(gameBoardId)
                .createdDate(gameBoard.getCreatedDate())
                .gameStatus(PLAYING)
                .timeSpend(0L)
                .board(board.getMaskedBoard())
                .build();
    }

    @Override
    public GameResponse updateGame(String username, UpdateGameRequest updateGameRequest) {
        logger.info("Updating a game for user {}", username);
        GameBoard gameBoard = getGameBoard(username, updateGameRequest.getGameId());

        Board board = boardMapper.gameBoardToBoard(gameBoard);

        if (!GAME_OVER.equals(gameBoard.getGameStatus())) {
            playGame(updateGameRequest, gameBoard, board);
        }

        return gameResponse(board, gameBoard);
    }

    @Override
    public List<GameResponse> getAll(String username) {
        List<GameBoard> gameBoards = gameBoardRepository.findByMinesweeperUser_Username(username);
        return gameBoards.stream().map(x -> gameResponseMapper.gameBoardToGameResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public GameResponse getGame(String username, Long gameId) {
        GameBoard gameBoard = getGameBoard(username, gameId);
        return gameResponseMapper.gameBoardToGameResponse(gameBoard);
    }

    private void playGame(UpdateGameRequest updateGameRequest, GameBoard gameBoard, Board board) {
        PlayStrategy playStrategy = strategyMap.get(updateGameRequest.getShootType());

        GameStatus gameStatus = playStrategy.execute(board, updateGameRequest);

        GameBoard updateGame = boardGameMapper.boardToGameBoard(board);
        gameBoard.setGameStatus(gameStatus);
        gameBoard.setBoard(updateGame.getBoard());
        gameBoard.setBoardMasked(updateGame.getBoardMasked());
        updateGameBoard(gameBoard);
    }

    private GameBoard getGameBoard(String username, Long id) {
        return gameBoardRepository.findByIdAndMinesweeperUser_Username(id, username)
                .orElseThrow(NotFoundGameException::new);
    }

    private GameResponse gameResponse(Board board, GameBoard gameBoard) {
        logger.info("Updating a game for user {} successful", gameBoard.getUsername());
        return GameResponse.builder()
                .gameStatus(gameBoard.getGameStatus())
                .timeSpend(getTimeSpend(gameBoard))
                .gameId(gameBoard.getId())
                .createdDate(gameBoard.getCreatedDate())
                .board(board.getMaskedBoard())
                .build();
    }

    private GameBoard saveNewGameBoard(String username, GameBoard board) {
        logger.info("Saving a new game for user {}", username);

        MinesweeperUser user = userService.getUser(username);
        board.setMinesweeperUser(user);

        LocalDateTime localDateTime = LocalDateTime.now();

        board.setCreatedDate(localDateTime);
        board.setLastTimeUpdated(localDateTime);
        board.setGameStatus(PLAYING);

        return saveGame(board);
    }

    private void updateGameBoard(GameBoard gameBoard) {
        gameBoard.setLastTimeUpdated(LocalDateTime.now());
        saveGame(gameBoard);
    }

    private GameBoard saveGame(GameBoard gameBoard) {
        GameBoard gameBoardSaved = gameBoardRepository.save(gameBoard);
        logger.info("Saving a new game for user {} successful", gameBoardSaved.getUsername());
        return gameBoardSaved;
    }

    private Long getTimeSpend(GameBoard gameBoard) {
        return Duration.between(gameBoard.getCreatedDate(), gameBoard.getLastTimeUpdated()).getSeconds();
    }

    private void putBombs(Board board) {
        for (int i = ZERO_INDEX; i < board.getBombsNumber(); ) {
            int randomColumn = random(board.getColumnsNumber());
            int randomRow = random(board.getRowsNumber());

            if (board.getCell(randomRow, randomColumn) != BOMB) {
                board.putMine(randomRow, randomColumn);
                fillNeighborhood(board, randomRow, randomColumn);
                i++;
            }
        }
    }

    private void fillNeighborhood(Board board, int randomRow, int randomColumn) {
        int minColumn = getMin(randomColumn);
        int maxColumn = getMax(randomColumn, board.getColumnsNumber());
        int minRow = getMin(randomRow);
        int maxRow = getMax(randomRow, board.getRowsNumber());

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                if (board.getCell(i, j) != BOMB) {
                    board.increaseCellValue(i, j);
                }
            }
        }
    }

    private int random(int columnsNumber) {
        return (int) (Math.random() * columnsNumber);
    }

    private int getMin(int number) {
        return number == ZERO_INDEX ? number : number - ONE_INDEX;
    }

    private int getMax(int number, int max) {
        return number + ONE_INDEX >= max ? number : number + ONE_INDEX;
    }
}
