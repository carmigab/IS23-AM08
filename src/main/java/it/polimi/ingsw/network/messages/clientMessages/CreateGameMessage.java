package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to create a game
 */
public class CreateGameMessage extends Message {
    /**
     * chosen number of player slots
     */
    private int num;

    /**
     * constructor
     * @param sender: the one who sends the message
     * @param num: number of player slots
     */
    public CreateGameMessage(String sender, int num) {
        super(sender);
        this.num = num;
        setMessageType("CreateGameMessage");
    }

    /**
     * method to get the number of player slots chosen
     * @return number of player slots
     */
    public int getNumberOfPlayers(){
        return this.num;

    }
}
