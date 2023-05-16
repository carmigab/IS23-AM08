package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.Player;

import java.util.Set;

public class BotLogic implements Player {
    /**
     * the nickname of the player
     */
    String myNickname;

    /**
     * the client that will be used to communicate with the server
     */
    Client client;

    /**
     * the game info that will be used to display the game
     */
    GameInfo gameInfo;

    /**
     * the current state of the game
     */
    State currentState;

    GameStateRepresentation gameStateRepresentation;

    ColorFitnessPerTile colorFitnessPerTile;

    public BotLogic(String myNickname, Client client, GameInfo gameInfo, State currentState) {
        this.myNickname = myNickname;
        this.client = client;
        this.gameInfo = gameInfo;
        this.currentState = currentState;

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

    @Override
    public boolean isMyTurn() {
        return myNickname.equals(gameInfo.getCurrentPlayerNickname());
    }

    public void play() {
        if (isMyTurn()) {

        }
    }

    private Set<Action> getAvailableActions() {
        return null;
    }
}
