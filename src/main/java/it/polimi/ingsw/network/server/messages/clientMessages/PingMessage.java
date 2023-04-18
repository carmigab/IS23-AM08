package it.polimi.ingsw.network.server.messages.clientMessages;

import it.polimi.ingsw.network.server.messages.Message;

public class PingMessage extends Message {
    public PingMessage(String text) {
        super(text);
    }
}
