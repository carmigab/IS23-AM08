package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the response to a ChatSomeone message
 */
public class ChatSomeoneResponse extends Message {
    /**
     * the constructor
     * @param sender: the one who sends the message
     */
    public ChatSomeoneResponse(String sender) {
        super(sender);
        setMessageType("ChatSomeoneResponse");
    }
}
