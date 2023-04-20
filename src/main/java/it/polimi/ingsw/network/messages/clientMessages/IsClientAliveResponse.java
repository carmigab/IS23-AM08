package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class IsClientAliveResponse extends Message {
    public IsClientAliveResponse(String sender) {
        super(sender);
    }
}
