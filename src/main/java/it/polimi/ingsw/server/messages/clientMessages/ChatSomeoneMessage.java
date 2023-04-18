package it.polimi.ingsw.server.messages.clientMessages;

import it.polimi.ingsw.server.messages.Message;

public class ChatSomeoneMessage extends Message {
    public ChatSomeoneMessage(String text) {
        super(text);
    }
}
