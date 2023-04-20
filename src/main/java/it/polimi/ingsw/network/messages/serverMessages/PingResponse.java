package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class PingResponse extends Message {
    public PingResponse(String sender) {
        super(sender);
    }
}
