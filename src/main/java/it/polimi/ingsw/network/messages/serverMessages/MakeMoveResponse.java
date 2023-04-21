package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class MakeMoveResponse extends Message {
    private boolean invalidMove = false;
    private boolean invalidNickname = false;

    public MakeMoveResponse(String sender, boolean invalidMove, boolean invalidNickname) {
        super(sender);
        this.invalidMove = invalidMove;
        this.invalidNickname = invalidNickname;
        setMessageType("MakeMoveResponse");
    }

    public boolean isInvalidMove(){
        return invalidMove;
    }

    public boolean isInvalidNickname(){
        return invalidNickname;
    }

}
