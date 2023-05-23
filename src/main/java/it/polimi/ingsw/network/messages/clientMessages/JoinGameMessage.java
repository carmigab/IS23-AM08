package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to join the game
 */
public class JoinGameMessage extends Message {

    private final String gameIndex;

    /**
     * constructor
     *
     * @param sender    : the sender
     * @param gameIndex
     */
    public JoinGameMessage(String sender, String gameIndex) {
        super(sender);
        this.gameIndex = gameIndex;
        setMessageType("JoinGameMessage");
    }

    public String getGameIndex() {
        return gameIndex;
    }
}
