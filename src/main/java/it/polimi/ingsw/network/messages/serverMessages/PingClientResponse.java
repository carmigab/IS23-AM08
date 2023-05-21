package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * The class represents the response to a IsServerAlive message
 */
public class PingClientResponse extends Message {
    /**
     * the constructor
     * @param sender: the one who sends the message
     */
    public PingClientResponse(String sender) {
        super(sender);
        setMessageType("PingClientResponse");
    }
}
