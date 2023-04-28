package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServerInterface extends Remote {
    public void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException, GameEndedException;

    public void messageSomeone(String message, String speaker, String receiver) throws RemoteException;

    public void messageAll(String message, String speaker) throws RemoteException;

    public boolean isAlive() throws RemoteException;
}
