package it.polimi.ingsw.gameInfo;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.GameEnded;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This immutable class is used for transferring the data to be updated from the server to the client
 */
public class GameInfo implements Serializable {
    /**
     * This attribute stores the current game board in the game
     */
    @Expose
    private final Tile[][] gameBoard;
    /**
     * This attribute stores the common goals created in the game
     */
    @Expose
    private final List<Integer> commonGoalsCreated;
    /**
     * This attribute stores the current stack of points for each common goal
     */
    @Expose
    private final List<Integer> commonGoalsStackTop;
    /**
     * This attribute stores all the states of all the players in the game
     */
    @Expose
    private final List<PlayerInfo> playerInfosList;

    /**
     * This attribute store the leader board of the game
     */
    @Expose
    private final List<GameEnded> leaderBoard;

    /**
     * This attribute stores the nickname of the current player
     */
    @Expose
    private final String currentPlayerNickname;

    /**
     * The constructor stores the references to copies of the GameModel attributes
     *
     * @param myGameBoard           reference to a copy of the game board
     * @param commonGoalsCreated    reference to a copy of the list of common goals
     * @param commonGoalsStackTop   reference to a copy of the stack of the common goals
     * @param playerInfosList       reference to a copy of all the player states
     * @param leaderBoard           reference to a copy of the leader board
     * @param currentPlayerNickname reference to the nickname of the current player
     */
    public GameInfo(Tile[][] myGameBoard, List<Integer> commonGoalsCreated, List<Integer> commonGoalsStackTop, List<PlayerInfo> playerInfosList, List<GameEnded> leaderBoard, String currentPlayerNickname){
        this.gameBoard =myGameBoard;
        this.commonGoalsCreated=commonGoalsCreated;
        this.commonGoalsStackTop=commonGoalsStackTop;
        this.playerInfosList=playerInfosList;
        this.leaderBoard = leaderBoard;
        this.currentPlayerNickname = currentPlayerNickname;
    }

    /**
     * Getter
     * @return the reference to the copied game board
     */
    public Tile[][] getGameBoard() {
        return gameBoard;
    }

    /**
     * Getter
     * @return the reference to the copied list of common goals created
     */
    public List<Integer> getCommonGoalsCreated() {
        return commonGoalsCreated;
    }

    /**
     * Getter
     * @return the reference to the copied stack of the common goals
     */
    public List<Integer> getCommonGoalsStack() {
        return commonGoalsStackTop;
    }

    /**
     * Getter
     * @return the reference to the copied list of player states
     */
    public List<PlayerInfo> getPlayerInfosList() {
        return playerInfosList;
    }

    /**
     * Getter
     * @return the current player nickname
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public List<GameEnded> getLeaderBoard() {
        return leaderBoard;
    }
}


