package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class MakeMoveResponse extends Message {
    private boolean invalidMove = false;
    private boolean invalidNickname = false;
    private boolean gameEnded = false;

    public MakeMoveResponse(String sender, boolean invalidMove, boolean invalidNickname, boolean gameEnded) {
        super(sender);
        this.invalidMove = invalidMove;
        this.invalidNickname = invalidNickname;
        this.gameEnded = gameEnded;
        setMessageType("MakeMoveResponse");
    }

    public boolean isInvalidMove(){
        return invalidMove;
    }

    public boolean isInvalidNickname(){
        return invalidNickname;
    }

    public boolean isGameEnded(){
        return gameEnded;
    }

}
