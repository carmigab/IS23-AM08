package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.RmiClient;
import it.polimi.ingsw.model.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServerInterface extends Remote {
    public boolean makeMove(List<Position> pos, int col, String nickname) throws RemoteException;

    // This exists only for debugging purposes
    public void registerPlayer(String nickname, RmiClient client) throws RemoteException;
}
