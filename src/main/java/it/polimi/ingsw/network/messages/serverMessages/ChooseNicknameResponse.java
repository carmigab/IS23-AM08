package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class ChooseNicknameResponse extends Message {
    private boolean response;
    public ChooseNicknameResponse(String sender, boolean response) {
        super(sender);
        this.response = response;
        setMessageType("ChooseNicknameResponse ");
    }

    public boolean getResponse(){
        return this.response;
    }

}
