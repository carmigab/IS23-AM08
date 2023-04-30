package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * The class represents the response to a IsServerAlive message
 */
public class IsServerAliveResponse extends Message {
    /**
     * the constructor
     * @param sender: the one who sends the message
     */
    public IsServerAliveResponse(String sender) {
        super(sender);
        setMessageType("PingClientResponse");
    }
}
