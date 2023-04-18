package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatAllMessage extends Message {
    private String chatMessage;
    public ChatAllMessage(String text, String chatMessage) {
        super(text);

        this.chatMessage = chatMessage;
    }

    public String getChatMessage(){
        return this.chatMessage;
    }
}
