package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public class Message implements Serializable{
    private final String sender;
    private String messageType = "Message";

    public Message(String sender) {
        this.sender = sender;
    }

    public String sender() {
        return this.sender;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return this.messageType;
    }
}
