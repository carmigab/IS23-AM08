package it.polimi.ingsw.view;

/**
 * This class is the view utilized by the application in gui mode
 */
public class GUIView extends View{

    /**
     * Method to display the information received from the server
     * It notifies the controller to update it
     */
    @Override
    protected void display() {
        this.controller.display();
    }

    /**
     * Method to welcome the user
     * It does nothing
     */
    @Override
    protected void welcome() {

    }

    /**
     * Method used to wait for a game to start
     * It does nothing
     */
    @Override
    protected void waitForGameStart() {

    }

    /**
     * Method to wait a specific command
     * It does nothing
     */
    @Override
    protected String waitCommand() {
        return null;
    }

    /**
     * Method to parse a specific command
     * It does nothing
     * @param command string to be parsed
     */
    @Override
    protected void parseCommand(String command) {

    }

    /**
     * Method used to choose connection type
     * It does nothing
     */
    @Override
    protected void chooseConnectionType() {

    }

    /**
     * Method used to ask the player for a nickname
     * It does nothing
     */
    @Override
    protected void askNickname() {

    }

    /**
     * Method used to ask the player if he wants to join or create a game
     * It does nothing
     */
    @Override
    protected void createOrJoinGame() {

    }

    /**
     * Method used to ask the player if he wants to play again
     * It does nothing
     */
    @Override
    protected boolean askIfWantToPlayAgain() {
        return false;
    }

    /**
     * Method used to notify the user when a connection is closed
     * It tells the controller to display the message in an alert pane
     */
    @Override
    protected void notifyClose(String message) {

        this.controller.showErrorAlert(message);

    }

    /**
     * Method used to notify the user when someone sends a message
     * It tells the controller to display the message received
     */
    @Override
    public void displayChatMessage(String message) {

        this.controller.displayMessage(message);

    }
}
