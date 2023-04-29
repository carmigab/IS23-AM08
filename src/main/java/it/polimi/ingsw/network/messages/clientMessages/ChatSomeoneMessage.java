package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represent the chat someone message
 */
public class ChatSomeoneMessage extends Message {
    /**
     * The message sent by the chat
     */
    private String chatMessage;
    /**
     * the one who is supposed to receive the chat message
     */
    private String receiver;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param chatMessage: the chat message
     * @param receiver: the one who is supposed to receive the message
     */
    public ChatSomeoneMessage(String sender, String chatMessage, String receiver) {
        super(sender);

        this.chatMessage = chatMessage;
        this.receiver = receiver;
        setMessageType("ChatSomeoneMessage");
    }

    /**
     * method to return the chat message
     * @return the chat message
     */
    public String getChatMessage(){
        return this.chatMessage;
    }

    /**
     * method to get the receiver of the chat message
     */
    public String getReceiver(){
        return this.receiver;
    }
}
