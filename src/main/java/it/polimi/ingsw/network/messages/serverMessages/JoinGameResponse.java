package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class JoinGameResponse extends Message {
    private boolean noGamesAvailable;
    private boolean nonExistentNickname;
    private boolean alreadyInGame;

    public JoinGameResponse(String sender, boolean noGamesAvailable, boolean nonExistentNickname, boolean alreadyInGame) {
        super(sender);

        this.alreadyInGame = alreadyInGame;
        this.noGamesAvailable = noGamesAvailable;
        this.nonExistentNickname = nonExistentNickname;
        setMessageType("JoinGameResponse");
    }

    public boolean isAlreadyInGame() {
        return alreadyInGame;
    }

    public boolean isNoGamesAvailable() {
        return noGamesAvailable;
    }

    public boolean isNonExistentNickname() {
        return nonExistentNickname;
    }
}
