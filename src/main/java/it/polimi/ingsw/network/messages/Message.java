package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * This class represents a message that is sent over tcp
 */
public class Message implements Serializable{
    /**
     * This attribute represents the one who sends the message
     */
    private final String sender;
    /**
     * This attribute is a string of the message type
     */
    private String messageType = "Message";

    /**
     * Constructor
     * @param sender: the one who sends the message
     */
    public Message(String sender) {
        this.sender = sender;
    }

    /**
     * Method to return the sender of the message
     * @return
     */
    public String sender() {
        return this.sender;
    }

    /**
     * Method to set a message type to the message
     * @param messageType: the message type
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * Method that returns the message type
     * @return the message type
     */
    @Override
    public String toString() {
        return this.messageType;
    }
}
