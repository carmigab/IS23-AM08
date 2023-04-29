package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to join the game
 */
public class JoinGameMessage extends Message {
    /**
     * constructor
     * @param sender: the sender
     */
    public JoinGameMessage(String sender) {
        super(sender);
        setMessageType("JoinGameMessage");
    }
}
