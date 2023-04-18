package it.polimi.ingsw.network.server.messages.clientMessages;

import it.polimi.ingsw.network.server.messages.Message;

public class ChatAllMessage extends Message {
    public ChatAllMessage(String text) {
        super(text);
    }
}
