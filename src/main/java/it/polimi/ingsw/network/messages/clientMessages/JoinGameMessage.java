package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents a message to join the game
 */
public class JoinGameMessage extends Message {

    private final String lobbyName;

    /**
     * constructor
     *
     * @param sender    : the sender
     * @param lobbyName : the lobby name
     */
    public JoinGameMessage(String sender, String lobbyName) {
        super(sender);
        this.lobbyName = lobbyName;
        setMessageType("JoinGameMessage");
    }

    /**
     * Method to get the lobby name
     * @return the lobby name
     */
    public String getLobbyName() {
        return lobbyName;
    }
}
