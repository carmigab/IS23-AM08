package it.polimi.ingsw.controller;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.observers.VirtualView;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.MatchServer;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is the controller of the pattern modern-view-controller; it accepts inputs and calls method from
 * class gameModel
 */

public class GameController {
    /**
     * this attribute is the model of a specific match
     */
    @Expose
    private final GameModel model;

    /**
     * this attribute is the list of players of a specific match
     */
    @Expose
    private final List<String> playersList;

    /**
     * this attribute is used to indicate the number of players of a specific match
     */
    @Expose
    private final int numPlayers;

    /**
     * this attribute is an observer by class VirtualView
     */
    private final VirtualView gameStateObserver;

    /**
     * this class is the class constructor; it receives the list of nicknames of the players, the number of the
     * players and the server where the controller resides and creates a new controller
     * @param nicknames nicknames of the players
     * @param numPlayers number of the players
     * @param server server where the controller resides
     */

    public GameController(List<String> nicknames, int numPlayers, MatchServer server){
        playersList = new ArrayList<>(nicknames);
        this.numPlayers = numPlayers;
        this.model = new GameModel(numPlayers, nicknames);

        this.gameStateObserver = new VirtualView(server);
        this.model.addObserver(gameStateObserver);

    }

    /**
     * This method initializes a new gameController from a pre-existing model
     * @param model: the model to load
     * @param server: the server where the controller resides
     */
    public GameController(GameModel model, MatchServer server){
        this.model = model;
        this.model.removeObservers();
        this.numPlayers = this.model.getPlayerListCopy().size();
        this.playersList = null;

        this.gameStateObserver = new VirtualView(server);
        this.model.addObserver(gameStateObserver);
    }

    /**
     * this method is the class constructor used to create a copy of an existing controller
     * @param controller the controller we want to copy
     */
    public GameController(GameController controller){
        this.playersList = controller.playersList;
        this.numPlayers = controller.numPlayers;
        this.model = new GameModel(controller.model);

        this.gameStateObserver = new VirtualView(null);
        this.model.addObserver(gameStateObserver);
    }

    /**
     * this method is used to verify the nickname of the current player and decide if the player is or not the
     * current one
     * @param nick the nickname we want to check
     * @return true if the nickname is correct, false otherwise
     */
    private boolean verifyPlayerNickname(String nick){
        return nick.equals(this.model.getCurrentPlayerState().getNickname());
    }

    /**
     * this method is used to evaluate a move done by a player; it checks if the move is correct (number of tiles,
     * position of the tiles ...) and then if the column chosen by the player is correct; it calls methods
     * checkValidMove and checkValidColumn by the class gameModel
     * @param pos list of tiles (positions) chosen by the player
     * @param col column chosen by the player
     * @return true if the move is correct, false otherwise
     */
    private boolean evaluationMove(List<Position> pos, int col){

        return model.checkValidMove(pos) && model.checkValidColumn(col, pos.size());
    }

    /**
     * this method is used to simulate a move done by a player; initially, it checks if the current player is
     * correct calling verifyPlayerNickname and if the move is correct calling evaluationMove; then the methods
     * calls makeMove from class GameModel and then nextTurn from class gameModel to pass the turn
     * @param pos list of position chosen by the player
     * @param col column chosen by the player
     * @param nick nickname of current player we want to check
     * @throws InvalidNicknameException if the nickname is wrong
     * @throws InvalidMoveException if the move is not correct
     */
    public void makeMove(List<Position> pos, int col, String nick) throws InvalidNicknameException, InvalidMoveException {
        if(!verifyPlayerNickname(nick)){
            throw new InvalidNicknameException();
        }
        if(!evaluationMove(pos, col)){
           throw new InvalidMoveException();
        }
        model.makeMove(pos, col);
        model.nextTurn();
    }

    /**
     * this method returns the current player of the game
     * @return an int, the current player of the game
     */
    public int getCurrentPlayer(){
        return this.model.getCurrentPlayer();
    }

    /**
     * This method forces the end of a game
     */
    public void forceGameOver(){
        this.model.forceEndGame(true);
    }




}
