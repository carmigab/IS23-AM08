package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class PingClientMessage extends Message {
    public PingClientMessage(String text) {
        super(text);
        setMessageType("PingClientMessage");
    }
}
