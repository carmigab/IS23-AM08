package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This method represents the response to a JoinGame message
 */
public class JoinGameResponse extends Message {
    /**
     * true if no game are available
     */
    private boolean noGamesAvailable;
    /**
     * true if the nickname does not exist
     */
    private boolean nonExistentNickname;
    /**
     * true if the player is already in game
     */
    private boolean alreadyInGame;

    private boolean wrongLobbyIndex;

    /**
     * the constructor
     *
     * @param sender              : the one who sends the message
     * @param noGamesAvailable    : true if no game are available
     * @param nonExistentNickname : true if the game does not exist
     * @param alreadyInGame       : true if the player is already in game
     * @param wrongLobbyIndex
     */
    public JoinGameResponse(String sender, boolean noGamesAvailable, boolean nonExistentNickname, boolean alreadyInGame, boolean wrongLobbyIndex) {
        super(sender);

        this.alreadyInGame = alreadyInGame;
        this.noGamesAvailable = noGamesAvailable;
        this.nonExistentNickname = nonExistentNickname;
        this.wrongLobbyIndex = wrongLobbyIndex;
        setMessageType("JoinGameResponse");
    }

    /**
     * Method to check if player already in game
     * @return true if the player is already in game
     */
    public boolean isAlreadyInGame() {
        return alreadyInGame;
    }

    /**
     * Method to check if no games are available
     * @return true if no game are available
     */
    public boolean isNoGamesAvailable() {
        return noGamesAvailable;
    }

    /**
     * Method to check if the nickname doesn't exist
     * @return true if the nickname does not exist
     */
    public boolean isNonExistentNickname() {
        return nonExistentNickname;
    }

    public boolean isWrongLobbyIndex() {
        return wrongLobbyIndex;
    }
}
