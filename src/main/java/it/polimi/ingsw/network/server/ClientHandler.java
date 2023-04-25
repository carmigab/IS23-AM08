package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

public class ClientHandler {
    RmiClientInterface rmiClient;
    TcpClientHandler tcpClient;

    boolean rmiInvocation;

    ClientHandler(RmiClientInterface rmiClient){
        this.rmiClient = rmiClient;
        this.rmiInvocation = true;
    }

    ClientHandler(TcpClientHandler tcpClient){
        this.tcpClient = tcpClient;
        this.rmiInvocation = false;
    }


    public void update(State newState, GameInfo newInfo) throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.update(newState, newInfo);
        else tcpClient.update(newState, newInfo);

    }

    public void isAlive() throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.isAlive();
        else tcpClient.isAlive();

    }

    public String name() throws RemoteException {
        if(rmiInvocation) return rmiClient.name();
        else return tcpClient.name();
    }

    public void receiveMessage(String message) throws RemoteException, TimeOutException {
        if(rmiInvocation) rmiClient.receiveMessage(message);
        else tcpClient.receiveMessage(message);
    }

    public void setMatchServer(MatchServer server){
        if(!rmiInvocation) tcpClient.setMatchServer(server);
    }
}
