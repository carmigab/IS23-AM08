package it.polimi.ingsw.network.client;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This is the Rmi Interface for the client
 */
public interface RmiClientInterface extends Remote {
    /**
     * This method has to be overridden in its subclasses, it is a remote invocation to update the client
     * @param newState: new state of the game
     * @param newInfo: new GameInfo
     * @throws RemoteException
     */
    public void update(State newState, GameInfo newInfo) throws RemoteException;

    /**
     * This method has to be overridden in its subclasses, it is a remote invocation to check that the client
     * is alive
     * @throws RemoteException
     */
    public void isAlive() throws RemoteException;

    /**
     * This method has to be overridden in its subclasses, it is a remote invocation to ask the client his nickname
     * @return the nickname
     * @throws RemoteException
     */
    public String name() throws RemoteException;

    /**
     * This method has to be overridden in its subclasses, it is a remote invocation to make the client receive a
     * message to be displayed in the chat
     * @param message the messages
     * @throws RemoteException
     */
    public void receiveMessage(String message) throws RemoteException;
}
