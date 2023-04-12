package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public abstract class Client extends UnicastRemoteObject {
    protected Client() throws RemoteException {
    }

    public boolean chooseNickname(String nick) throws RemoteException {
        return false;
    }

    public boolean makeMove(List<Position> pos, int col) throws RemoteException, InvalidIdException, InvalidMoveException{
        return false;
    }

    public void createGame(int num) throws RemoteException, NotBoundException{}

    public void joinGame() throws RemoteException, NotBoundException, NoGamesAvailableException {}

    public void messageSomeone(String message, String receiver) throws RemoteException{}

    public void messageAll(String message) throws RemoteException{}

}
