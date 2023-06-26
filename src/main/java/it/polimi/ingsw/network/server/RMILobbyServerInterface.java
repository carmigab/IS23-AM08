package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.server.exceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface contains the methods that can be called by the client on the Lobby Server
 */
public interface RMILobbyServerInterface extends Remote {
    /**
     * This method is called by the client to choose a nickname
     * @param nickname the nickname chosen by the player
     * @return true if the nickname is valid, false otherwise
     * @throws RemoteException if the connection is lost
     * @throws ExistentNicknameException if the nickname is already taken
     * @throws IllegalNicknameException if the nickname is illegal
     */
    public boolean chooseNickname(String nickname) throws RemoteException, ExistentNicknameException, IllegalNicknameException;

    /**
     * This method is called by the client to create a new game
     * @param numPlayers the number of players in the game
     * @param nickname  the nickname of the player who created the game
     * @param client the client who created the game
     * @return lobby name
     * @throws RemoteException if the connection is lost
     * @throws AlreadyInGameException if the player is already in a game
     * @throws NonExistentNicknameException if the nickname is not valid
     */
    public String createGame(Integer numPlayers, String nickname, RmiClientInterface client) throws RemoteException, AlreadyInGameException, NonExistentNicknameException;

    /**
     * This method is called by the client to join a game
     * @param nickname the nickname of the player who wants to join the game
     * @param client the client who wants to join the game
     * @param gameIndex the index of the game to join
     * @return lobby name
     * @throws RemoteException if the connection is lost
     * @throws NoGamesAvailableException if there are no games available
     * @throws AlreadyInGameException if the player is already in a game
     * @throws NonExistentNicknameException if the nickname is not valid
     * @throws NoGameToRecoverException if there are no games available for recovery
     * @throws WrongLobbyIndexException if the lobby index is not valid
     * @throws LobbyFullException if the lobby is full
     */
    public String joinGame(String nickname, RmiClientInterface client, String gameIndex) throws RemoteException, NoGamesAvailableException, AlreadyInGameException, NonExistentNicknameException, NoGameToRecoverException, WrongLobbyIndexException, LobbyFullException;

    /**
     * This method is called by the client get the list of available games
     * @param nickname the nickname of the player who wants to get the list of available games
     * @return the list of available games
     * @throws RemoteException if the connection is lost
     * @throws NoGamesAvailableException if there are no games available
     */
    List<Lobby> getLobbies(String nickname) throws RemoteException, NoGamesAvailableException;

    /**
     * This method is called by the client to recover a game
     * @param nickname the nickname of the player who wants to recover the game
     * @param rmiClient the client who wants to recover the game
     * @return lobby name
     * @throws RemoteException if the connection is lost
     * @throws NoGameToRecoverException if there are no games available for recovery
     */
    String recoverGame(String nickname, RmiClientInterface rmiClient) throws RemoteException, NoGameToRecoverException;
}
