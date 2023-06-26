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
 * this class is the controller part of the pattern Modern-View-Controller. it accepts input and calls methods of
 * the model (in particular calls method of the class GameModel in the model)
 */

public class GameController {

    /**
     * this attribute represents the model of the game
     */
    @Expose
    private final GameModel model;

    /**
     * this attribute is the list of the player of the game
     */
    @Expose
    private final List<String> playersList;

    /**
     * this attribute is used to store the number of the player of a match
     */
    @Expose
    private final int numPlayers;

    /**
     * this attribute is the observer of the gameState; it is a VirtualView, a middle class between view and
     * controller
     */
    private final VirtualView gameStateObserver;

    /**
     * this method is the class constructor: it receives a list of nicknames, the number of the players and a server
     * and create a new controller
     * @param nicknames nicknames of the players
     * @param numPlayers number of players
     * @param server server where the controller resides
     *
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
     * this method is another class constructor used to create a copy of an existing controller
     * @param controller the controller of which we want de copy
     */
    public GameController(GameController controller){
        this.playersList = controller.playersList;
        this.numPlayers = controller.numPlayers;
        this.model = new GameModel(controller.model);

        this.gameStateObserver = new VirtualView(null);
        this.model.addObserver(gameStateObserver);
    }

    /**
     * this method is used to check if a nickname coincides with the correct one
     * @param nick the nickname we want to verify
     * @return true if the nickname is correct
     */
    private boolean verifyPlayerNickname(String nick){
        return nick.equals(this.model.getCurrentPlayerState().getNickname());
    }

    /**
     * this method is used to check a single move : we check if the move is correct (number of tiles, position of
     * tiles...) and also we check if the column chosen by the player is correct. this method calls methods
     * checkValidMove and checkValidColumn of class gameModel
     * @param pos list of tiles chosen by the player
     * @param col column chosen by the player
     * @return true if the move is correct, false otherwise
     */
    private boolean evaluationMove(List<Position> pos, int col){

        return model.checkValidMove(pos) && model.checkValidColumn(col, pos.size());
    }

    /**
     * this method is used to simulate a move of a player; initially, we check if the nickname is correct and if
     * the move is correct (call the private method of this class), then the method calls makeMove of class
     * GameModel and then nextTurn, always of class GameModel, to pass the turn
     * @param pos list of tiles chosen by the player
     * @param col column chosen by the player
     * @param nick nickname of the current player
     * @throws InvalidNicknameException if the nickname is not correct
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
     * this method return the current player of the game
     * @return an int, the current player (return the specific position in the playerState list)
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
