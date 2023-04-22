package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class IsServerAliveMessage extends Message {
    public IsServerAliveMessage(String text) {
        super(text);
        setMessageType("PingClientMessage");
    }
}
