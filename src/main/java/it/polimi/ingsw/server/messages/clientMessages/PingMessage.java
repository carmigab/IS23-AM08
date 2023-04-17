package it.polimi.ingsw.server.messages.clientMessages;

import it.polimi.ingsw.server.messages.Message;

public class PingMessage extends Message {
    public PingMessage(String text) {
        super(text);
    }
}
