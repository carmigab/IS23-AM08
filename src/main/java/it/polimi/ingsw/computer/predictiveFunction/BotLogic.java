package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class BotLogic extends View {

    private RmiClient client;

    private GameStateRepresentation gameStateRepresentation;

    private ColorFitnessPerTile colorFitnessPerTile;

    private Map<Move, Action> moveActionMap;

    public BotLogic(String myNickname, String gameName) {
        this.myNickname = myNickname;
        this.moveActionMap = new HashMap<>();

        try {
            this.client = new RmiClient(this.myNickname, this, "localhost", ServerConstants.RMI_PORT);
            this.client.connectToMatchServer(gameName);
        }catch (RemoteException | NotBoundException | InterruptedException e) {
            System.out.println("bot unable to connect to server");
        }

        colorFitnessPerTile = new ColorFitnessPerTile();
    }

    @Override
    public void update(State newState, GameInfo newGameInfo) {
        this.currentState = newState;
        this.gameInfo = newGameInfo;

        System.out.println("bot update");

        Tile[][] shelf = gameInfo.getPlayerInfosList().stream()
                .filter(playerInfo -> playerInfo.getNickname().equals(myNickname))
                .map(PlayerInfo::getShelf)
                .findFirst()
                .orElseThrow();

//        for (SingleGoal singleGoal: gameInfo.getPlayerInfosList().stream()
//                .filter(playerInfo -> playerInfo.getNickname().equals(myNickname))
//                .map(PlayerInfo::getPersonalGoal)
//                .findFirst()
//                .orElseThrow()) {
//
//            shelf[singleGoal.getPosition().y()][singleGoal.getPosition().x()] = new Tile(singleGoal.getColor(), 0);
//        }

        if (gameStateRepresentation == null) {
            gameStateRepresentation = new GameStateRepresentation(gameInfo.getGameBoard(), shelf,
                    gameInfo.getPlayerInfosList().stream()
                            .filter(playerInfo -> playerInfo.getNickname().equals(myNickname))
                            .map(PlayerInfo::getPersonalGoal)
                            .findFirst()
                            .orElseThrow());
        }
        else {
            gameStateRepresentation.setBoard(gameInfo.getGameBoard());
            gameStateRepresentation.setShelf(shelf);
        }

        colorFitnessPerTile.updateColorFitnessPerTile(gameStateRepresentation);

        play();
    }

    public void play() {
        if (isMyTurn()) {
            Action action = getBestAction();

            Move move = mapActionToMove(action);

            System.out.println("Best move: ");
            for(Position p: move.positions()) System.out.println(p.x()+" "+p.y());

            try {
                client.makeMove(move.positions(), move.column());
                moveActionMap.clear();
            } catch (InvalidNicknameException | InvalidMoveException | ConnectionError | GameEndedException e) {
                System.out.println("bot unable to make move");
            }
        }
    }

    private Action mapMoveToAction(Move move) {
        List<Position> positions = move.positions();

        List<TileColor> tileColors = positions.stream().
                map(position -> gameStateRepresentation.getBoard()[position.y()][position.x()].getColor())
                .toList();

        Action action = new Action(tileColors, move.column());

        moveActionMap.put(move, action);

        return action;
    }

    private Move mapActionToMove(Action action) {
        return moveActionMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(action))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    private Action getBestAction() {
        Action bestAction = null;
        Set<Action> availableActions = getAvailableActions();
        double maxFitness = -10;

        for (Action action: availableActions) {
            if (colorFitnessPerTile.evaluateAction(action, gameStateRepresentation.getShelf()) > maxFitness) {
                maxFitness = colorFitnessPerTile.evaluateAction(action, gameStateRepresentation.getShelf());
                bestAction = action;
            }
        }

        return bestAction;
    }

    private Set<Action> getAvailableActions() {
        Set<Action> availableActions = new HashSet<>();

        List<Position> availablePositions = getAdj(new ArrayList<>());

        for (Position position: availablePositions) {
            availableActions.addAll(getSingleTileActions(position));
            availableActions.addAll(getDoubleTileActions(position));
            availableActions.addAll(getTripleTileActions(position));
        }

        return availableActions;
    }

    private Collection<? extends Action> getSingleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 1)) {
                moves.add(new Move(List.of(position), i));
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    private Collection<? extends Action> getDoubleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 2)) {
                List<Position> adj = getAdj(List.of(position));

                for (Position adjPosition: adj) {
                    moves.add(new Move(List.of(position, adjPosition), i));
                }
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    private Collection<? extends Action> getTripleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 3)) {
                List<Position> adj = getAdj(List.of(position));

                for (Position adjPosition: adj) {
                    List<Position> adj2 = getAdj(List.of(position, adjPosition));

                    for (Position adj2Position: adj2) {
                        moves.add(new Move(List.of(position, adjPosition, adj2Position), i));
                    }
                }
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    /**
     * This method is called by update to display the game
     */
    @Override
    protected void display() {

    }

    /**
     * This method is called by getUserInput to welcome the player
     */
    @Override
    protected void welcome() {

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

    public RmiClientInterface getRMIClientInterface() {
        return this.client;
    }
}
