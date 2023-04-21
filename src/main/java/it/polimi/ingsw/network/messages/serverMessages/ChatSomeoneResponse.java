package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatSomeoneResponse extends Message {
    public ChatSomeoneResponse(String sender) {
        super(sender);
        setMessageType("ChatSomeoneResponse");
    }
}
