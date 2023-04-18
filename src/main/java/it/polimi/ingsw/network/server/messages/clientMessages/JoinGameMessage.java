package it.polimi.ingsw.network.server.messages.clientMessages;

import it.polimi.ingsw.network.server.messages.Message;

public class JoinGameMessage extends Message {
    public JoinGameMessage(String text) {
        super(text);
    }
}
