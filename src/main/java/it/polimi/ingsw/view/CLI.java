package it.polimi.ingsw.view;

/**
 * This class is used to represent the CLI view of the game
 */
public class CLI extends View{

    /**
     * This method is called by update to display the game
     */
    @Override
    protected void display() {

    }

    /**
     * This method is called by start to wait for a command from the player
     *
     * @return the command represented as a string
     */
    @Override
    public String waitCommand() {
        return null;
    }

    /**
     * This method is called by start to ask the player if he wants to connect via rmi or socket
     */
    @Override
    public void chooseConnectionType() {

    }

    /**
     * This method is called by start to ask the player his nickname and send it to the server
     */
    @Override
    public void askNickname() {

    }

    /**
     * This method is called by start to ask the player if he wants to create a new game or join an existing one
     */
    @Override
    public void createOrJoinGame() {

    }

    /**
     * This method is called by start to ask the player if he wants to play again
     *
     * @return true if the player wants to play again, false otherwise
     */
    @Override
    public boolean askIfWantToPlayAgain() {
        return false;
    }
}
