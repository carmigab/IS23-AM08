package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.messages.Message;

public class UpdateMessage extends Message {
    private State newState;
    private GameInfo newInfo;

    public UpdateMessage(String sender, State newState, GameInfo newInfo) {
        super(sender);
        this.newInfo = newInfo;
        this.newState = newState;
        setMessageType("UpdateMessage");
    }

    public State getNewState() {
        return newState;
    }

    public GameInfo getNewInfo(){
        return  newInfo;
    }
}
