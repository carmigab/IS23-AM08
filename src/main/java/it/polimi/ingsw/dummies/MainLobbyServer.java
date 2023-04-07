package it.polimi.ingsw.dummies;

import it.polimi.ingsw.server.LobbyServer;

import java.rmi.RemoteException;

public class MainLobbyServer {
    public static void main(String[] args) {

        try {
            LobbyServer ls= new LobbyServer();
            ls.start();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
