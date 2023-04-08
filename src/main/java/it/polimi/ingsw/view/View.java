package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.gameInfo.GameInfo;

/**
 * This abstract class is used to represent the view of the game
 */
public abstract class View {
    /**
     * the nickname of the player
     */
    private String myNickname;

    /**
     * the client that will be used to communicate with the server
     */
    private Client client;

    /**
     * the game info that will be used to display the game
     */
    private GameInfo gameInfo;

    /**
     * if true the player wants to play
     */
    private boolean iWantToPlay = true;

    /**
     * This method is called by the server to update the view
     * @param gameInfo the new game info, it will be used to update the view
     */
    public void update(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        display();
    }

    /**
     * This method is called by update to display the game
     */
    protected abstract void display();

    /**
     * This method is called by the client main to start the view
     */
    public void start() {
        chooseConnectionType();
        askNickname();
        while (iWantToPlay) {
            createOrJoinGame();
            String command;
            while (!gameInfo.isGameEnded()) {
                command = waitCommand();
                parseCommand(command);
            }
            iWantToPlay = askIfWantToPlayAgain();
        }
    }

    /**
     * This method is called by start to wait for a command from the player
     * @return the command represented as a string
     */
    protected abstract String waitCommand();

    /**
     * This method is called by waitCommand to parse the command and call the right method
     * @param command the command to parse
     */
    //TODO: implementare
    private void parseCommand(String command) {
    }

    /**
     * This method is called by start to ask the player if he wants to connect via rmi or socket
     */
    protected abstract void chooseConnectionType();

    /**
     * This method is called by start to ask the player his nickname and send it to the server
     */
    protected abstract void askNickname();
    // cosa deve fare, chiedi nickname, controlla su server se va bene (chooseNickname), se va bene salvalo in myNickname
    // se no ripeti

    /**
     * This method is called by start to ask the player if he wants to create a new game or join an existing one
     */
    protected abstract void createOrJoinGame();

    /**
     * This method is called by parseCommand to check if it's the player's turn, if not it will not send the command to the server
     * @return true if it's the player's turn, false otherwise
     */
    private boolean isMyTurn(){
        return myNickname.equals(gameInfo.getCurrentPlayerNickname());
    }

    /**
     * This method is called by parseCommand to check if the move is valid, if not it will not send the command to the server
     * @return true if the move is valid, false otherwise
     */
    //TODO: implementare
    private boolean checkValidMove(){
        return true;
    }

    /**
     * This method is called by start to ask the player if he wants to play again
     * @return true if the player wants to play again, false otherwise
     */
    protected abstract boolean askIfWantToPlayAgain();
}
