package it.polimi.ingsw.network.server.messages.clientMessages;

import it.polimi.ingsw.network.server.messages.Message;

public class CreateGameMessage extends Message {
    public CreateGameMessage(String text) {
        super(text);
    }
}
