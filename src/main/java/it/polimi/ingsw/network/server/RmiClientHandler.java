package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

/**
 * This is the class that handles the tcp client server side
 */
public class RmiClientHandler extends ClientHandler {
    /**
     * This attribute is the rmiClient
     */
    private RmiClientInterface rmiClient;

    /**
     * This is the constructor
     * @param rmiClient: the rmi client
     */
    RmiClientHandler(RmiClientInterface rmiClient){
        this.rmiClient = rmiClient;
    }

    /**
     * This method is called by the matchServer and sends a message to update the client with the
     * new state and new game info
     * @param newState: the new state
     * @param newInfo: the new game info
     * @throws RemoteException  if the client is not online
     */
    public void update(State newState, GameInfo newInfo) throws RemoteException {
        rmiClient.update(newState, newInfo);
    }

    /**
     * This method throws and exception if the client is not online
     * @throws RemoteException if the client is not online
     */
    public void isAlive() throws RemoteException {
        rmiClient.isAlive();
    }

    /**
     * This method is called by the matchServer and returns the nickname of the player
     * @return the nickname of the player
     */
    public String name() throws RemoteException {
        return rmiClient.name();
    }

    /**
     * This method sends a chat message to the client
     * @param message: the chat message
     * @throws RemoteException if the client is not online
     */
    public void receiveMessage(String message) throws RemoteException {
        rmiClient.receiveMessage(message);
    }

}
