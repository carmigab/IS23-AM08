package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChooseNicknameMessage extends Message {
    String nick;

    public ChooseNicknameMessage(String sender, String nick) {
        super(sender);
        this.nick = nick;
        setMessageType("ChooseNicknameMessage");
    }

    public String getNick(){
        return this.nick;

    }
}
