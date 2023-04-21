package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class CreateGameMessage extends Message {
    private int num;

    public CreateGameMessage(String text, int num) {
        super(text);
        this.num = num;
        setMessageType("CreateGameMessage");
    }

    public int getNumberOfPlayers(){
        return this.num;

    }
}
