package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class JoinGameMessage extends Message {
    public JoinGameMessage(String text) {
        super(text);
        setMessageType("JoinGameMessage");
    }
}
