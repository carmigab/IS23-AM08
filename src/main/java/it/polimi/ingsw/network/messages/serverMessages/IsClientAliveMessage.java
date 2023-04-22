package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class IsClientAliveMessage extends Message {
    public IsClientAliveMessage(String sender) {
        super(sender);
        setMessageType("IsClientAlive");
    }
}
