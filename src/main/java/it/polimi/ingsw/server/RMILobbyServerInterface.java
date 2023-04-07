package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface contains the methods that can be called on the client at the start of the application
 */
public interface RMILobbyServerInterface extends Remote {

    public boolean chooseNickname(String nickname) throws RemoteException;

    public ConnectionInformationRMI createGame(Integer numPlayers, String nickname, RmiClient client) throws RemoteException;

    public ConnectionInformationRMI joinGame(String nickname, RmiClient client) throws RemoteException;

}
