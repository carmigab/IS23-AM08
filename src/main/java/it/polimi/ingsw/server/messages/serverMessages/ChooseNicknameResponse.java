package it.polimi.ingsw.server.messages.serverMessages;

import it.polimi.ingsw.server.messages.Message;

public class ChooseNicknameResponse extends Message {
    private boolean flag;
    public ChooseNicknameResponse(String sender) {
        super(sender);
    }

    public void setResponse(boolean b){
        this.flag = b;
    }

    public boolean getResponse(){
        return this.flag;
    }

}
