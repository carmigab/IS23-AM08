package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class IsServerAliveResponse extends Message {
    public IsServerAliveResponse(String sender) {
        super(sender);
        setMessageType("PingClientResponse");
    }
}
