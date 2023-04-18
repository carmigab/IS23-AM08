package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;

public class MainServer {
    public static void main(String[] args) throws RemoteException {

        int port = 1888;

        LobbyServer lobbyS = new LobbyServer();
        lobbyS.start();




    }
}
