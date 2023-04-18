package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class CreateGameResponse extends Message {
    private boolean nonExistentNickname = false;
    private boolean alreadyInGame = false;

    public CreateGameResponse(String sender, boolean nonExistentNickname, boolean alreadyInGame) {
        super(sender);
        this.nonExistentNickname = nonExistentNickname;
        this.alreadyInGame = alreadyInGame;
    }

    public boolean isAlreadyInGame() {
        return this.alreadyInGame;
    }

    public boolean isNonExistentNickname() {
        return this.nonExistentNickname;
    }
}
