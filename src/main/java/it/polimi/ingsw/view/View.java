package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.GameEnded;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    protected State currentState = State.WAITINGFORPLAYERS;

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
        this.checkForShutdown();
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
            welcome();
            createOrJoinGame();
            waitForGameStart();
            String command;
            while (!currentState.equals(State.ENDGAME) && !currentState.equals(State.GRACEFULDISCONNECTION)) {
                // wait for the player's command
                command = waitCommand();

                // parse the command and send it to the server
                parseCommand(command); // il client deve gestire la remote exception e settare shutdown a true
            }

            iWantToPlay = false;
            // ask the player if he wants to play again
            if (!currentState.equals(State.GRACEFULDISCONNECTION)) {
                iWantToPlay = askIfWantToPlayAgain();
            }
        }
        close("The session has ended\nThank you for playing with us!");
    }

    /**
     * This method is called by getUserInput to welcome the player
     */
    protected abstract void welcome();

    /**
     * This method is called by getUserInput to wait for other players to join the game
     */
    protected abstract void waitForGameStart();

    /**
     * This method is launched in a new thread to check if the client has crashed
     */
    public void checkForShutdown() {
        //while (true) {
            if (currentState.equals(State.GRACEFULDISCONNECTION)) {
                close("One player has crashed, the game will be closed...\nThank you for playing with us!");
            }
           // try {
             //   wait();
         //   } catch (InterruptedException e) {
           //     throw new RuntimeException(e);
          //  }
        //}
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
     * This method is used to check the single position: the client choose a tile and this method check if the
     * position is in the board, if the tile isn't invalid or empty and if the tile has free adjacent
     */
    private boolean checkSinglePosition(Position pos){
        if(pos.x() < 0 || pos.x() >= ModelConstants.BOARD_DIMENSION) return false;
        if(pos.y() < 0 || pos.y() >= ModelConstants.BOARD_DIMENSION) return false;
        if(this.gameInfo.getGameBoard()[pos.y()] [pos.x()].isEmpty() || this.gameInfo.getGameBoard()[pos.y()] [pos.x()].isInvalid()){
            return false;
        }
        return UtilityFunctionsModel.hasFreeAdjacent(this.gameInfo.getGameBoard(), pos);
    }

    /**
     * this method is used to check if the position is valid : it calls method checkSinglePosition and getAdj on list
     * positions : if checkSinglePosition returns true and getAdj(positions) contains pos, return true
     *
     * @param positions list of positions already choose by the player
     * @param pos new position chosen by the player
     * @return true if checkSinglePosition returns true and getAdj called on list positions contains pos
     */

    protected boolean checkValidPosition(List<Position> positions, Position pos)  {
        return checkSinglePosition(pos) && getAdj(positions).contains(pos);
    }

    /**
     * this method return the list of the Position the player can choose after the previous moves
     * @param pos list of positions already chosen by the player
     * @return the list of the Position the player can choose after the previous moves
     */

    protected List<Position> getAdj(List<Position> pos){
        List<Position> result = new ArrayList<>();
        if(pos.isEmpty()){
            for (int i = 0; i < ModelConstants.BOARD_DIMENSION; i++) {
                for (int j = 0; j < ModelConstants.BOARD_DIMENSION; j++) {
                    result.add(new Position(i, j));
                }
            }
        }
        else if(pos.size() == 1){
            for(Position p : pos){
                result.add(new Position(p.x(), p.y()+1));
                result.add(new Position(p.x(), p.y()-1));
                result.add(new Position(p.x()+1, p.y()));
                result.add(new Position(p.x()-1, p.y()));
            }
        }
        else{
            if(pos.get(0).x() == pos.get(1).x()){
                result.add(new Position(pos.get(0).x(), Math.min(pos.get(0).y() - 1, pos.get(1).y() - 1)));
                result.add(new Position(pos.get(0).x(), Math.max(pos.get(0).y() + 1, pos.get(1).y() + 1)));
            }
            else{
                result.add(new Position(Math.min(pos.get(0).x() - 1, pos.get(1).x() - 1), pos.get(0).y()));
                result.add(new Position(Math.max(pos.get(0).x() + 1, pos.get(1).x() + 1), pos.get(0).y()));
            }
        }

        return reduceAdjacent(result);
    }



    /**
     * this method receive the List of the next position the player can choose after the previous moves and return the
     * List of the next positions that are not empty or invalid
     * @param adj list of the next position the player can choose after the previous moves
     * @return the list the next position the player can choose after the previous moves that are not empty or invalid
     */
    private List<Position> reduceAdjacent(List<Position> adj){
       return adj.stream().filter(pos -> checkSinglePosition(pos)).collect(Collectors.toList());
    }

    /**
     * this method check if the column is valid
     * @param col the column selected by the player
     * @param numTiles the number of tiles taken by the player
     * @return true if the column is valid, false if is not valid
     */


    protected boolean checkColumn(int col, int numTiles){
        if(col < 0 || col >= ModelConstants.COLS_NUMBER) return false;
        List<PlayerInfo>  player = this.gameInfo.getPlayerInfosList();
        for(int i=0; i<player.size();i++){
            if(player.get(i).getNickname().equals(gameInfo.getCurrentPlayerNickname())){
                return UtilityFunctionsModel.getFreeSpaces(this.gameInfo.getPlayerInfosList().get(i).getShelf(), col) >= numTiles;
            }
        }
        return false;
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

    protected String getLeaderBoardAsText(){
        StringBuilder toReturn= new StringBuilder();

        for(GameEnded g : gameInfo.getLeaderBoard()){
            toReturn.append(g.getNickname()).append(": ").append(g.getFinalPoints()).append(" points\n");
        }

        return toReturn.toString();
    }


    protected GameViewController controller;

    public void setGameViewController(GameViewController controller){
        this.controller=controller;
    }
}
