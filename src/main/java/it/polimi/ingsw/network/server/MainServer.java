package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;

/**
 * Main server of the application
 */
public class MainServer {
    public static void main(String[] args) throws RemoteException {

        LobbyServer lobbyS = new LobbyServer();
        lobbyS.start();




    }
}
