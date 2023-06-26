package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.exceptions.*;

import java.util.List;

/**
 * This is the client interface, it makes the communication protocol transparent
 * to the rest of the program
 */
public interface Client  {

    /**
     * This method has to be overridden in its subclasses, it asks the server a
     * possible nickname
     * @param nick the nickname
     * @return true if successful
     * @throws ConnectionError if there is a connection error
     */
    public boolean chooseNickname(String nick) throws ConnectionError;

    /**
     * This method has to be overridden in its subclasses, it tries to make a move
     * @param pos: list of tiles positions
     * @param col: column
     * @throws InvalidNicknameException if the nickname does not exist
     * @throws InvalidMoveException if the move is not valid
     * @throws InvalidNicknameException if the nickname does not exist
     * @throws ConnectionError if there is a connection error
     * @throws GameEndedException if the game has ended
     */
    public void makeMove(List<Position> pos, int col) throws InvalidMoveException, InvalidNicknameException, ConnectionError, GameEndedException;

    /**
     * This method has to be overridden in its subclasses, it tries to create a new game
     * @param num: number of players
     * @throws NonExistentNicknameException if the nickname does not exist
     * @throws AlreadyInGameException if the player is already in a game
     * @throws ConnectionError if there is a connection error
     */
    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException,ConnectionError;

    /**
     * This method has to be overridden in its subclasses, it tries to recover a new game
     * @throws NoGameToRecoverException if there is no game to recover
     * @throws ConnectionError if there is a connection error
     */
    public void recoverGame() throws NoGameToRecoverException, ConnectionError;


    /**
     * This method has to be overridden in its subclasses, it tries to join a new game
     * @throws NoGamesAvailableException if there are no active lobbies
     * @throws NonExistentNicknameException if the nickname does not exist
     * @throws AlreadyInGameException if the player is already in a game
     * @throws ConnectionError if there is a connection error
     * @throws WrongLobbyIndexException if the lobby index is wrong
     * @throws LobbyFullException if the lobby is full
     */
    public void joinGame(String lobbyName) throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException, NoGameToRecoverException, ConnectionError, WrongLobbyIndexException, LobbyFullException;

    /**
     * This method has to be overridden in its subclasses, it tries to send a private message
     * @param message: the message
     * @param receiver: the one supposed to receive the message
     * @throws ConnectionError if there is a connection error
     */
    public void messageSomeone(String message, String receiver) throws ConnectionError;

    /**
     * This method has to be overridden in its subclasses, it tries to send a public message
     * @param message: the message
     * @throws ConnectionError if there is a connection error
     */
    public void messageAll(String message) throws ConnectionError;

    /**
     * This method retrieve the active lobbies on the server
     * @return the list of the active lobbies
     * @throws NoGamesAvailableException if there are no active lobbies
     * @throws ConnectionError if there is a connection error
     */
    public List<Lobby> getLobbies() throws NoGamesAvailableException, ConnectionError;
}
