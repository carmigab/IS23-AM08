package it.polimi.ingsw.controller;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.observers.VirtualView;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.RmiServer;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    @Expose
    private final GameModel model;
    @Expose
    private final List<String> playersList;
    @Expose
    private final int numPlayers;

    private final VirtualView gameStateObserver;

    public GameController(List<String> nicknames, int numPlayers, RmiServer server){
        playersList = new ArrayList<>(nicknames);
        this.numPlayers = numPlayers;
        this.model = new GameModel(numPlayers, nicknames);
        this.gameStateObserver = new VirtualView(server);
        this.model.addObserver(gameStateObserver);

    }

    public GameController(GameController controller){
        this.playersList = controller.playersList;
        this.numPlayers = controller.numPlayers;
        this.model = new GameModel(controller.model);
        this.gameStateObserver = new VirtualView(null); //TODO (pass the rmiServer by parameter)
        this.model.addObserver(gameStateObserver);
    }

    private boolean verifyPlayerNickname(String nick){
        return nick.equals(this.model.getPlayer().getNickname());
    }

    private boolean evaluationMove(List<Position> pos, int col){

        return model.checkValidMove(pos) && model.checkValidColumn(col, pos.size());
    }

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

    public int getCurrentPlayer(){
        return this.model.getCurrentPlayer();
    }

    /**
     * This method forces the end of a game
     */
    public void forceGameOver(){
        this.model.endGame(true);
    }




}
