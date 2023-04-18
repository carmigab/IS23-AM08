package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChooseNicknameMessage extends Message {

    public ChooseNicknameMessage(String nick) {
        super(nick);
    }

    public String getNick(){
        return this.sender();
    }
}
