package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

import java.util.List;

/**
 * This abstract class is used to represent the view of the game
 */
public abstract class View {
    /**
     * the nickname of the player
     */
    protected String myNickname;

    /**
     * the client that will be used to communicate with the server
     */
    protected Client client;

    /**
     * the game info that will be used to display the game
     */
    protected GameInfo gameInfo;

    /**
     * the current state of the game
     */
    protected State currentState;

    /**
     * if true the player wants to play
     */
    protected boolean iWantToPlay = true;

    /**
     * This method is called by the server to update the view
     * @param newState the new state of the game, it will be used to update the view
     * @param newGameInfo the new game info, it will be used to update the view
     */
    public void update(State newState, GameInfo newGameInfo) {
        this.currentState = newState;
        this.gameInfo = newGameInfo;
        display();
    }

    /**
     * This method is called by update to display the game
     */
    protected abstract void display();

    /**
     * This method is called by the client main to start the view
     */
    public synchronized void getUserInput() {
        chooseConnectionType();
        askNickname();
        while (iWantToPlay) {
            createOrJoinGame();
            waitForGameStart();
            String command;
            while (!currentState.equals(State.ENDGAME) && !currentState.equals(State.GRACEFULDISCONNECTION)) {
                // wait for the player's command
                command = waitCommand();

                // parse the command and send it to the server
                parseCommand(command); // il client deve gestire la remote exception e settare shutdown a true
            }
            // ask the player if he wants to play again
            if (!currentState.equals(State.GRACEFULDISCONNECTION)) {
                iWantToPlay = askIfWantToPlayAgain();
            }
        }
        close("Thank you to have played with us, bye bye");
    }

    /**
     * This method is called by getUserInput to wait for other players to join the game
     */
    protected abstract void waitForGameStart();

    /**
     * This method is launched in a new thread to check if the client has crashed
     */
    public synchronized void checkForShutdown() {
        while (true) {
            if (currentState != null && currentState.equals(State.GRACEFULDISCONNECTION)) {
                close("One player has crashed, the game will be closed");
            }
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method is called by start to wait for a command from the player
     * @return the command represented as a string
     */
    protected abstract String waitCommand(); // serve timer per evitare che il giocatore si blocca e lancia eccezzione

    /**
     * This method is called by waitCommand to parse the command and call the right method
     * @param command the command to parse
     */
    //TODO: implementare
    protected abstract void parseCommand(String command);

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
    protected boolean isMyTurn(){
        return myNickname.equals(gameInfo.getCurrentPlayerNickname());
    }

    /**
     * This method is called by parseCommand to check if the move is valid, if not it will not send the command to the server
     * @return true if the move is valid, false otherwise
     */
    //TODO: implementare
    protected boolean checkValidMove(List<Position> tiles, int column){
        return true;
    }

    /**
     * This method is called by start to ask the player if he wants to play again
     * @return true if the player wants to play again, false otherwise
     */
    protected abstract boolean askIfWantToPlayAgain();

    /**
     * This method tell the user that the client is shutting down
     */
    protected void close(String message) {
        notifyClose(message);
        System.exit(0);
    }

    /**
     * This method is called by close to notify the player that the client is shutting down
     * @param message the message to display
     */
    protected abstract void notifyClose(String message);

    public abstract void displayChatMessage(String message);
}
