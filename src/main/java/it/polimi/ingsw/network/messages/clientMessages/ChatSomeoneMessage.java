package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChatSomeoneMessage extends Message {
    private String chatMessage;
    private String receiver;

    public ChatSomeoneMessage(String text, String chatMessage, String receiver) {
        super(text);

        this.chatMessage = chatMessage;
        this.receiver = receiver;
    }

    public String getChatMessage(){
        return this.chatMessage;
    }

    public String getReceiver(){
        return this.receiver;
    }
}
