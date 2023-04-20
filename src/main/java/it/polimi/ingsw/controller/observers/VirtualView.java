package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.server.MatchServer;

import java.util.HashMap;

/**
 * This class is used for storing the information relative to the current game state which has
 * to be sent to each client that is currently playing the game
 */
public class VirtualView implements Observer{

    /**
     * This attribute stores the information of the current state of the game.
     * The client will read the state and do its moves accordingly
     */
    private State currentState;

    /**
     * This attribute stores the information that the client has to receive and process.
     */
    private GameInfo gameInfo;

    private final MatchServer server;


    public VirtualView(MatchServer server){
        this.server=server;
        this.currentState=State.TURN0;
        this.gameInfo=null;
    }


    /**
     * This method is the override of the observer pattern
     * It updates the gameInfo which has to be sent to the client with copies of what is present in the model
     * TODO: And also tells the server to update
     * @param model that will be observed
     */
    @Override
    public void update(GameModel model) {
        // Something bad happens when we create a new gameInfo
        this.gameInfo=new GameInfo(model.getGameBoardCopy(), model.getCommonGoalsCreatedCopy(), model.getCommonGoalsStackCopy(), model.getPlayerListCopy(), new HashMap<>(), model.getCurrentPlayerNickName());
        if(model.isGameOver()) this.currentState=State.ENDGAME;
        else {
            switch (model.getCurrentPlayer()) {
                case 0 -> this.currentState = State.TURN0;
                case 1 -> this.currentState = State.TURN1;
                case 2 -> this.currentState = State.TURN2;
                case 3 -> this.currentState = State.TURN3;
                default -> {
                }
            }
        }
        this.server.update(this.currentState,this.gameInfo);
    }
}
