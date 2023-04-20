package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatReceiveMessage extends Message {
    private String chatMessage;

    public ChatReceiveMessage(String sender, String chatMessage) {
        super(sender);
        this.chatMessage = chatMessage;
    }

    public String getChatMessage(){
        return this.chatMessage;
    }
}
