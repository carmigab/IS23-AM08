package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to choose the nickname
 */
public class ChooseNicknameMessage extends Message {
    /**
     * the chosen nickname
     */
    String nick;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param nick: the chosen nickname
     */
    public ChooseNicknameMessage(String sender, String nick) {
        super(sender);
        this.nick = nick;
        setMessageType("ChooseNicknameMessage");
    }

    /**
     * method to return the chosen nickname
     * @return the chosen nickname
     */
    public String getNick(){
        return this.nick;

    }
}
