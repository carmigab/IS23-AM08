package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public class BotLogic extends View {

    private RmiClient client;

    private GameStateRepresentation gameStateRepresentation;

    private ColorFitnessPerTile colorFitnessPerTile;

    public BotLogic(String myNickname, String gameName, GameInfo gameInfo, State state) {
        this.myNickname = myNickname;
        this.gameInfo = gameInfo;
        this.currentState = state;

        try {
            this.client = new RmiClient(this.myNickname, this, "localhost", ServerConstants.RMI_PORT);
            this.client.connectToMatchServer(gameName);
        }catch (RemoteException | NotBoundException | InterruptedException e) {
            System.out.println("bot unable to connect to server");
        }

        Tile[][] shelf = gameInfo.getPlayerInfosList().stream()
                .filter(playerInfo -> playerInfo.getNickname().equals(myNickname))
                .map(PlayerInfo::getShelf)
                .findFirst()
                .orElseThrow();

        gameStateRepresentation = new GameStateRepresentation(gameInfo.getGameBoard(), shelf);

        colorFitnessPerTile = new ColorFitnessPerTile();
        colorFitnessPerTile.updateColorFitnessPerTile(gameStateRepresentation);
    }

    @Override
    public void update(State newState, GameInfo newGameInfo) {
        this.currentState = newState;
        this.gameInfo = newGameInfo;

        Tile[][] shelf = gameInfo.getPlayerInfosList().stream()
                .filter(playerInfo -> playerInfo.getNickname().equals(myNickname))
                .map(PlayerInfo::getShelf)
                .findFirst()
                .orElseThrow();

        gameStateRepresentation = new GameStateRepresentation(gameInfo.getGameBoard(), shelf);

        colorFitnessPerTile.updateColorFitnessPerTile(gameStateRepresentation);

        play();
    }

    public void play() {
        if (isMyTurn()) {
            Action action = getBestAction();

            List<Position> selectedTiles = mapActionToPositions(action);

            try {
                client.makeMove(selectedTiles, action.getColumn());
            } catch (InvalidNicknameException | InvalidMoveException | ConnectionError | GameEndedException e) {
                System.out.println("bot unable to make move");
            }
        }
    }

    private List<Position> mapActionToPositions(Action action) {
        return null;
    }

    private Action getBestAction() {
        Set<Action> availableActions = getAvailableActions();

        return null;
    }

    private Set<Action> getAvailableActions() {
        return null;
    }

    /**
     * This method is called by update to display the game
     */
    @Override
    protected void display() {

    }

    /**
     * This method is called by getUserInput to wait for other players to join the game
     */
    @Override
    protected void waitForGameStart() {

    }

    /**
     * This method is called by start to wait for a command from the player
     *
     * @return the command represented as a string
     */
    @Override
    protected String waitCommand() {
        return null;
    }

    /**
     * This method is called by waitCommand to parse the command and call the right method
     *
     * @param command the command to parse
     */
    @Override
    protected void parseCommand(String command) {

    }

    /**
     * This method is called by start to ask the player if he wants to connect via rmi or socket
     */
    @Override
    protected void chooseConnectionType() {

    }

    /**
     * This method is called by start to ask the player his nickname and send it to the server
     */
    @Override
    protected void askNickname() {

    }

    /**
     * This method is called by start to ask the player if he wants to create a new game or join an existing one
     */
    @Override
    protected void createOrJoinGame() {

    }

    /**
     * This method is called by start to ask the player if he wants to play again
     *
     * @return true if the player wants to play again, false otherwise
     */
    @Override
    protected boolean askIfWantToPlayAgain() {
        return false;
    }

    /**
     * This method is called by close to notify the player that the client is shutting down
     *
     * @param message the message to display
     */
    @Override
    protected void notifyClose(String message) {

    }

    @Override
    public void displayChatMessage(String message) {

    }
}
