package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

/**
 * This is the abstract class that handles the client server side
 */
public abstract class ClientHandler {
    /**
     * This method is called by the matchServer and sends a message to update the client with the
     * new state and new game info
     * @param newState: the new state
     * @param newInfo: the new game info
     * @throws TimeOutException
     * @throws RemoteException
     */
    public void update(State newState, GameInfo newInfo) throws RemoteException, TimeOutException {}

    /**
     * This method throws and exception if the client is not online
     * @throws TimeOutException
     * @throws RemoteException
     */
    public void isAlive() throws RemoteException, TimeOutException {}

    /**
     * This method is called by the matchServer and returns the nickname of the player
     * @return the nickname of the player
     * @throws RemoteException
     */
    public String name() throws RemoteException {
        return null;
    }

    /**
     * This method sends a chat message to the client
     * @param message: the chat message
     * @throws TimeOutException
     * @throws RemoteException
     */
    public void receiveMessage(String message) throws RemoteException, TimeOutException {}

    /**
     * This method sets the match server
     * @param matchServer: the match server
     */
    public void setMatchServer(MatchServer matchServer){}
}
