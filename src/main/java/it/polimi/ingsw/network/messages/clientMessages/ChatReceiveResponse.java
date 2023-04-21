package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatReceiveResponse extends Message {
    public ChatReceiveResponse(String sender) {
        super(sender);
        setMessageType("ChatReceiveResponse");
    }
}
