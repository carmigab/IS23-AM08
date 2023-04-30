package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the chat message htat the client is supposed to receive
 */
public class ChatReceiveMessage extends Message {
    /**
     * This attribute represents the chat message
     */
    private String chatMessage;

    /**
     * The constructor
     * @param sender: the one who sends the message
     * @param chatMessage: the chat message
     */
    public ChatReceiveMessage(String sender, String chatMessage) {
        super(sender);
        this.chatMessage = chatMessage;
        setMessageType("ChatReceiveMessage");
    }

    /**
     * Method to get the chat message
     * @return the chat message
     */
    public String getChatMessage(){
        return this.chatMessage;
    }
}
