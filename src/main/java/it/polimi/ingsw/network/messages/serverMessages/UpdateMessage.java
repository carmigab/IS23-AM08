package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to update the client
 */
public class UpdateMessage extends Message {
    /**
     * the new state
     */
    private State newState;
    /**
     * the new gameInfo
     */
    private GameInfo newInfo;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param newState: the new state
     * @param newInfo: the new gameInfo
     */
    public UpdateMessage(String sender, State newState, GameInfo newInfo) {
        super(sender);
        this.newInfo = newInfo;
        this.newState = newState;
        setMessageType("UpdateMessage");
    }

    /**
     * method to get the new state
     * @return the new state
     */
    public State getNewState() {
        return newState;
    }

    /**
     * method to get the new gameInfo
     * @return the new gameInfo
     */
    public GameInfo getNewInfo(){
        return  newInfo;
    }
}
