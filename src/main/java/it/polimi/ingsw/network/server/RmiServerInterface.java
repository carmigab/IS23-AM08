package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface contains the methods that can be called by the client on the MatchServer
 */
public interface RmiServerInterface extends Remote {
    /**
     * This method is used to join a game
     * @param pos the position chosen by the player
     * @param col the column chosen by the player
     * @param nickname the nickname chosen by the player
     * @throws RemoteException if the connection is lost
     * @throws InvalidNicknameException if the nickname is invalid
     * @throws InvalidMoveException if the move is invalid
     * @throws GameEndedException if the game is ended
     */
    public void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException, GameEndedException;

    /**
     * This method is used to send a chat message
     * @param message the message to be sent
     * @param speaker the nickname of the player who sent the message
     * @param receiver the nickname of the player who will receive the message
     * @throws RemoteException if the connection is lost
     */
    public void messageSomeone(String message, String speaker, String receiver) throws RemoteException;

    /**
     * This method is used to send a chat message to all the players in the game
     * @param message the message to be sent
     * @param speaker the nickname of the player who sent the message
     * @throws RemoteException if the connection is lost
     */
    public void messageAll(String message, String speaker) throws RemoteException;

    /**
     * This method is used to ping the server or the client
     * @return true if the server/client is alive, false otherwise
     * @throws RemoteException if the connection is lost
     */
    public boolean isAlive() throws RemoteException;
}
