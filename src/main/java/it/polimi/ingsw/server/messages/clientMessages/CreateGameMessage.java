package it.polimi.ingsw.server.messages.clientMessages;

import it.polimi.ingsw.server.messages.Message;

public class CreateGameMessage extends Message {
    public CreateGameMessage(String text) {
        super(text);
    }
}
