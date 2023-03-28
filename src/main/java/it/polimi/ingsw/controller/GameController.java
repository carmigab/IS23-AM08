package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GameModel model;
    private final List<String> playersList;
    private final int numPlayers;

    public GameController(List<String> nicknames, int numPlayers){
        playersList = new ArrayList<>(nicknames);
        this.numPlayers = numPlayers;
        this.model = new GameModel(numPlayers, nicknames);
    }

    private boolean verifyPlayerId(int playerId){
        return playerId == this.model.getCurrentPlayer();
    }

    private boolean evaluationMove(List<Position> pos, int col){

        return model.checkValidMove(pos) && model.checkValidColumn(col, pos.size());
    }

    public void makeMove(List<Position> pos, int col, int playerId) throws InvalidIdException, InvalidMoveException {
        if(!verifyPlayerId(playerId)){
            throw new InvalidIdException();
        }
        if(!evaluationMove(pos, col)){
           throw new InvalidMoveException();
        }
        model.makeMove(pos, col);
        model.nextTurn();
    }



}
