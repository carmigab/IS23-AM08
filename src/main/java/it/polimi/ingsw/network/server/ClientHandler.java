package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

/**
 * This class wraps the rmiClientInterfaces and TcpClientHandlers, and makes the server transparent to the
 * communication protocol of the client
 */
public class ClientHandler {
    /**
     * This attribute represents a rmi remote object
     */
    RmiClientInterface rmiClient;
    /**
     * This attribute represents a tcp client handler
     */
    TcpClientHandler tcpClient;
    /**
     * This flag is true we call methods on the remote object
     */
    boolean rmiInvocation;


    /**
     * the constructor with the rmi client
     * @param rmiClient: the rmi client
     */
    ClientHandler(RmiClientInterface rmiClient){
        this.rmiClient = rmiClient;
        this.rmiInvocation = true;
    }

    /**
     * the constructor with the rmi client
     * @param tcpClient: the tcp client
     */
    ClientHandler(TcpClientHandler tcpClient){
        this.tcpClient = tcpClient;
        this.rmiInvocation = false;
    }

    /**
     * This method updates the client with a new state and a new game info
     * @param newState: the new state of the game
     * @param newInfo: the new game info
     * @throws RemoteException
     * @throws TimeOutException
     */
    public void update(State newState, GameInfo newInfo) throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.update(newState, newInfo);
        else tcpClient.update(newState, newInfo);

    }

    /**
     * This method pings the client
     * @throws RemoteException
     * @throws TimeOutException
     */
    public void isAlive() throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.isAlive();
        else tcpClient.isAlive();

    }

    /**
     * This method asks the client for his nickname
     * @return the nickname
     * @throws RemoteException
     */
    public String name() throws RemoteException {
        if(rmiInvocation) return rmiClient.name();
        else return tcpClient.name();
    }

    /**
     * This method sends a chat message to the client
     * @param message: the chat message
     * @throws RemoteException
     * @throws TimeOutException
     */
    public void receiveMessage(String message) throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.receiveMessage(message);
        else tcpClient.receiveMessage(message);
    }

    /**
     * This method sets the matchServer of the tcp client if present
     * @param server: the matchServer
     */
    public void setMatchServer(MatchServer server){
        if(!rmiInvocation) tcpClient.setMatchServer(server);
    }
}
