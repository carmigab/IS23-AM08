package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class IsClientAlive extends Message {
    public IsClientAlive(String sender) {
        super(sender);
        setMessageType("IsClientAlive");
    }
}
