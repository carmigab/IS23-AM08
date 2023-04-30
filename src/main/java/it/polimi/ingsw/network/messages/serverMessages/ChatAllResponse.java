package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the response to a ChatAll message
 */
public class ChatAllResponse extends Message {
    /**
     * The constructor
     * @param sender: the one who sends the message
     */
    public ChatAllResponse(String sender) {
        super(sender);
        setMessageType("ChatAllResponse");
    }
}
