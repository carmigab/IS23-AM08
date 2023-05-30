package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

public class RecoverGameResponse extends Message {

    /**
     * true if no game are available
     */
    private boolean noGamesAvailable;

    /**
     * Constructor
     *
     * @param sender : the one who sends the message
     */
    public RecoverGameResponse(String sender, boolean noGamesAvailable) {
        super(sender);

        this.noGamesAvailable=noGamesAvailable;
        setMessageType("RecoverGameResponse");
    }

    /**
     * Method to check if no games are available
     * @return true if no game are available
     */
    public boolean isNoGamesAvailable() {
        return noGamesAvailable;
    }
}
