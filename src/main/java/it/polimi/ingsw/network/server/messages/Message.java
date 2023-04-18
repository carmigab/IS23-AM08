package it.polimi.ingsw.network.server.messages;

import java.io.Serializable;

public class Message implements Serializable{
    private final String sender;

    public Message(String sender) {
        this.sender = sender;
    }

    public String sender() {
        return this.sender;
    }
}
