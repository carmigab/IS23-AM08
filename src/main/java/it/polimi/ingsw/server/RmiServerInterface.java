package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClientInterface;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServerInterface extends Remote {
    public void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException;

    // This exists only for debugging purposes
    public void registerPlayer(String nickname, RmiClientInterface client) throws RemoteException;

    public void messageSomeone(String message, String speaker, String receiver) throws RemoteException;

    public void messageAll(String message, String speaker) throws RemoteException;

    public boolean isAlive() throws RemoteException;
}
