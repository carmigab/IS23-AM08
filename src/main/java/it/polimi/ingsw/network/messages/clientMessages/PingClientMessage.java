package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to ask the server if he is alive (ping)
 */
public class PingClientMessage extends Message {
    /**
     * The constructor
     * @param sender: the one who sends the message
     */
    public PingClientMessage(String sender) {
        super(sender);
        setMessageType("PingClientMessage");
    }
}
