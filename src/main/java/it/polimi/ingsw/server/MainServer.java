package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.RmiServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
    public static void main(String[] args) throws RemoteException {

        int port = 1888;

        LobbyServer lobbyS = new LobbyServer();
        lobbyS.start();




    }
}
