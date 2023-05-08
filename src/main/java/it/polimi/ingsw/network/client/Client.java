package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;

import java.util.List;

public interface Client  {

    public boolean chooseNickname(String nick) throws ConnectionError;

    public void makeMove(List<Position> pos, int col) throws InvalidNicknameException, InvalidMoveException, InvalidNicknameException, ConnectionError, GameEndedException;

    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException,ConnectionError;

    public void createGameWithComputer(int num) throws NonExistentNicknameException, AlreadyInGameException,ConnectionError;

    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException, ConnectionError;

    public void messageSomeone(String message, String receiver) throws ConnectionError;

    public void messageAll(String message) throws ConnectionError;

}
