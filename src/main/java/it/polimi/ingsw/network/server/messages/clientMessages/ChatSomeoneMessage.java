package it.polimi.ingsw.network.server.messages.clientMessages;

import it.polimi.ingsw.network.server.messages.Message;

public class ChatSomeoneMessage extends Message {
    public ChatSomeoneMessage(String text) {
        super(text);
    }
}
