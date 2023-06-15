package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to recover a game
 */
public class RecoverGameMessage extends Message {
    /**
     * Constructor
     *
     * @param sender : the sender
     */
    public RecoverGameMessage(String sender) {
        super(sender);
        setMessageType("RecoverGameMessage");
    }
}
