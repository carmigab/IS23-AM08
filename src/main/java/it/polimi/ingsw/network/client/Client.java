package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;

import java.util.List;

public interface Client  {

    public boolean chooseNickname(String nick);

    public void makeMove(List<Position> pos, int col) throws InvalidNicknameException, InvalidMoveException, InvalidNicknameException;

    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException;

    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException;

    public void messageSomeone(String message, String receiver);

    public void messageAll(String message);

}
