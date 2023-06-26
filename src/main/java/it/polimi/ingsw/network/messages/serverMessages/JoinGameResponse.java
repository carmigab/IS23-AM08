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
    /**
     * true if there are no games available for recovery
     */
    private boolean noGameToRecover;
    /**
     * true if the lobby index is not valid
     */
    private boolean wrongLobbyIndex;
    /**
     * true if the lobby is full
     */
    private boolean lobbyFull;

    /**
     * the constructor
     *
     * @param sender              : the one who sends the message
     * @param noGamesAvailable    : true if no game are available
     * @param nonExistentNickname : true if the game does not exist
     * @param noGameToRecover     : true if there are no games available for recovery
     * @param alreadyInGame       : true if the player is already in game
     * @param wrongLobbyIndex    : true if the lobby index is not valid
     * @param lobbyFull          : true if the lobby is full
     */
    public JoinGameResponse(String sender, boolean noGamesAvailable, boolean nonExistentNickname, boolean noGameToRecover, boolean alreadyInGame, boolean wrongLobbyIndex, boolean lobbyFull) {
        super(sender);

        this.alreadyInGame = alreadyInGame;
        this.noGamesAvailable = noGamesAvailable;
        this.nonExistentNickname = nonExistentNickname;
        this.noGameToRecover = noGameToRecover;
        this.wrongLobbyIndex = wrongLobbyIndex;
        this.lobbyFull = lobbyFull;
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

    /**
     * Method to check if there are no games available for recovery
     * @return true if there are no games available for recovery
     */
    public boolean isNoGameToRecover() {return noGameToRecover;}

    /**
     * Method to check if the lobby index is not valid
     * @return true if the lobby index is not valid
     */
    public boolean isWrongLobbyIndex() {
        return wrongLobbyIndex;
    }

    /**
     * Method to check if the lobby is full
     * @return true if the lobby is full
     */
    public boolean isLobbyFull() { return lobbyFull;
    }
}
