package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represent a message to send a public chat message
 */
public class ChatAllMessage extends Message {
    /**
     * The chat message to send
     */
    private String chatMessage;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param chatMessage: the chat message
     */
    public ChatAllMessage(String sender, String chatMessage) {
        super(sender);

        this.chatMessage = chatMessage;
        setMessageType("ChatAllMessage");
    }

    /**
     * method to get the chat message
     * @return the chat message
     */
    public String getChatMessage(){
        return this.chatMessage;
    }
}
