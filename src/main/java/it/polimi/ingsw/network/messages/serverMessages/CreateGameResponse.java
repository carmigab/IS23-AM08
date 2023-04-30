package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This method represents the response to a CreateGame message
 */
public class CreateGameResponse extends Message {
    /**
     * true if the nickname does not exist
     */
    private boolean nonExistentNickname = false;
    /**
     * true if the player is already in game
     */
    private boolean alreadyInGame = false;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param nonExistentNickname: true if the game does not exist
     * @param alreadyInGame: true if the player is already in game
     */
    public CreateGameResponse(String sender, boolean nonExistentNickname, boolean alreadyInGame) {
        super(sender);
        this.nonExistentNickname = nonExistentNickname;
        this.alreadyInGame = alreadyInGame;
        setMessageType("CreateGameResponse");
    }

    /**
     * Method to check if player already in game
     * @return true if the player is already in game
     */
    public boolean isAlreadyInGame() {
        return this.alreadyInGame;
    }

    /**
     * Method to check if the nickname doesn't exist
     * @return true if the nickname does not exist
     */
    public boolean isNonExistentNickname() {
        return this.nonExistentNickname;
    }
}
