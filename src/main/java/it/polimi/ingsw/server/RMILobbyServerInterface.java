package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.client.RmiClientInterface;
import it.polimi.ingsw.server.exceptions.ExistentNicknameExcepiton;
import it.polimi.ingsw.server.exceptions.IllegalNicknameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface contains the methods that can be called on the client at the start of the application
 */
public interface RMILobbyServerInterface extends Remote {

    public boolean chooseNickname(String nickname) throws RemoteException, ExistentNicknameExcepiton, IllegalNicknameException;

    public ConnectionInformationRMI createGame(Integer numPlayers, String nickname, RmiClientInterface client) throws RemoteException;

    public ConnectionInformationRMI joinGame(String nickname, RmiClientInterface client) throws RemoteException, NoGamesAvailableException;

}
