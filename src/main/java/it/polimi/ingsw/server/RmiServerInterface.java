package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServerInterface extends Remote {
    public boolean makeMove(List<Position> pos, int col, String nickname) throws RemoteException;

}