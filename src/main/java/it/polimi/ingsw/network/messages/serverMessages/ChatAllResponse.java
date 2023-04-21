package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatAllResponse extends Message {
    public ChatAllResponse(String sender) {
        super(sender);
        setMessageType("ChatAllResponse");
    }
}
