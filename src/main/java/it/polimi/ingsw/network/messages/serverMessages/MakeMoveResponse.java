package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a response to a MakeMoveMessage
 */
public class MakeMoveResponse extends Message {
    /**
     * thag that is true if the move is invalid
     */
    private boolean invalidMove = false;
    /**
     * flag that is true if the nickname is invalid
     */
    private boolean invalidNickname = false;
    /**
     * flag that is true if the game has ended
     */
    private boolean gameEnded = false;

    /**
     * the constructor
     * @param sender: the one who sends a message
     * @param invalidMove: true if the move is invalid
     * @param invalidNickname: true if the nickname is invalif
     * @param gameEnded: true if the game has ended
     */
    public MakeMoveResponse(String sender, boolean invalidMove, boolean invalidNickname, boolean gameEnded) {
        super(sender);
        this.invalidMove = invalidMove;
        this.invalidNickname = invalidNickname;
        this.gameEnded = gameEnded;
        setMessageType("MakeMoveResponse");
    }

    /**
     * method to check if a move is invalid
     * @return invalid move flag
     */
    public boolean isInvalidMove(){
        return invalidMove;
    }

    /**
     * method to check if a nickname is invalid
     * @return invalid nickname flag
     */
    public boolean isInvalidNickname(){
        return invalidNickname;
    }

    /**
     * method to check if a game has ended
     * @return the game ended flag
     */
    public boolean isGameEnded(){
        return gameEnded;
    }

}
