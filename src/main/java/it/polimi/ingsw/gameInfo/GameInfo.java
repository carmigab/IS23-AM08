package it.polimi.ingsw.gameInfo;

import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This immutable class is used for transferring the data to be updated from the server to the client
 */
public class GameInfo implements Serializable {
    /**
     * This attribute stores the current game board in the game
     */
    private final Tile[][] myGameBoard;
    /**
     * This attribute stores the common goals created in the game
     */
    private final List<Integer> commonGoalsCreated;
    /**
     * This attribute stores the current stack of points for each common goal
     */
    private final List<List<Integer>> commonGoalsStack;
    /**
     * This attribute stores all the states of all the players in the game
     */
    private final List<PlayerState> playerStatesList;

    /**
     * This attribute is true if the game has ended
     */
    private boolean gameEnded;

    /**
     * This attribute store the leader board of the game
     */
    private Map<String, Integer> leaderBoard;

    /**
     * This attribute stores the nickname of the current player
     */
    private String currentPlayerNickname;

    /**
     * The constructor stores the references to copies of the GameModel attributes
     * @param myGameBoard reference to a copy of the game board
     * @param commonGoalsCreated reference to a copy of the list of common goals
     * @param commonGoalsStack reference to a copy of the stack of the common goals
     * @param playerStatesList reference to a copy of all the player states
     */
    public GameInfo(Tile[][] myGameBoard, List<Integer> commonGoalsCreated, List<List<Integer>> commonGoalsStack, List<PlayerState> playerStatesList){
        this.myGameBoard=myGameBoard;
        this.commonGoalsCreated=commonGoalsCreated;
        this.commonGoalsStack=commonGoalsStack;
        this.playerStatesList=playerStatesList;
    }

    /**
     * Getter
     * @return the reference to the copied game board
     */
    public Tile[][] getMyGameBoard() {
        return myGameBoard;
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
    public List<List<Integer>> getCommonGoalsStack() {
        return commonGoalsStack;
    }
    /**
     * Getter
     * @return the reference to the copied list of player states
     */
    public List<PlayerState> getPlayerStatesList() {
        return playerStatesList;
    }

    /**
     * Getter
     * @return true if the game has ended
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Getter
     * @return the current player nickname
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}
