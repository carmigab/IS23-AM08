package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class PingClientResponse extends Message {
    public PingClientResponse(String sender) {
        super(sender);
        setMessageType("PingClientResponse");
    }
}
