package it.polimi.ingsw.view;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.Client;

public interface Player {
//    /**
//     * the nickname of the player
//     */
//    String myNickname;
//
//    /**
//     * the client that will be used to communicate with the server
//     */
//    Client client;
//
//    /**
//     * the game info that will be used to display the game
//     */
//    GameInfo gameInfo;
//
//    /**
//     * the current state of the game
//     */
//    GameStateRepresentation currentState;

    void update(State newState, GameInfo newGameInfo);

    boolean isMyTurn();
}
