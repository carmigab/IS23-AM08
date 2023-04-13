package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.server.exceptions.NonExistentNicknameException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public abstract class Client extends UnicastRemoteObject {
    protected Client() throws RemoteException {
    }

    public boolean chooseNickname(String nick) {
        return false;
    }

    public void makeMove(List<Position> pos, int col) throws InvalidNicknameException, InvalidMoveException, InvalidNicknameException {
    }

    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException {}

    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException {}

    public void messageSomeone(String message, String receiver) {}

    public void messageAll(String message) {}

}
