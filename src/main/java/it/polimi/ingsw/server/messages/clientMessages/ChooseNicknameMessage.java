package it.polimi.ingsw.server.messages.clientMessages;

import it.polimi.ingsw.server.messages.Message;

public class ChooseNicknameMessage extends Message {

    public ChooseNicknameMessage(String nick) {
        super(nick);
    }

    public String getNick(){
        return this.sender();
    }
}
