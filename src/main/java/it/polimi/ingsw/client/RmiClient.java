package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.dummies.FakeView;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.ConnectionInformationRMI;
import it.polimi.ingsw.server.RMILobbyServerInterface;
import it.polimi.ingsw.server.RmiServerInterface;
import it.polimi.ingsw.server.exceptions.ExistentNicknameExcepiton;
import it.polimi.ingsw.server.exceptions.IllegalNicknameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RmiClient extends Client implements RmiClientInterface{
    private String nickname;

    private int lobbyPort = 1999;
    private String LobbyServerName = "LobbyServer";

    private RmiServerInterface matchServer;
    private RMILobbyServerInterface lobbyServer;

    // Here we will use an observer or the true view
    private FakeView view;


    /**
     * TO COMPLETE (true view)
     * This method is the constructor of RmiClient
     * @param nickname
     * @param fV : this will become the true view
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RmiClient(String nickname, FakeView fV) throws RemoteException, NotBoundException, InterruptedException {
        super();
        this.view = fV;
        this.nickname = nickname;

        // to comment in case of test without LobbyServer
        this.connectToLobbyServer();
    }

    /**
     * This method looks up the registry of the lobby server
     * If it doesn't found it he waits for 5 second
     * @throws RemoteException
     * @throws NotBoundException
     */
    private void connectToLobbyServer() throws RemoteException, NotBoundException, InterruptedException {
        while(true) {
            try {
                System.out.println("Looking up the registry for LobbyServer");
                Registry registry = LocateRegistry.getRegistry(lobbyPort);
                this.lobbyServer = (RMILobbyServerInterface) registry.lookup(LobbyServerName);
                break;
            } catch (Exception e) {
                System.out.println("Registry not found");
                Thread.sleep(5000);
            }
        }
    }

    /**
     * This method updates the view with new information
     * @param newState : the new state of the game
     * @param newInfo : the new info for the view
     * @throws RemoteException
     */
    @Override
    public void update(State newState, GameInfo newInfo) throws RemoteException {
        this.view.update(newState, newInfo);
    }

    /**
     * This method lets the player choose his nickname
     * @param nick
     * @return true if nickname is available
     * @throws RemoteException
     */
    // To do
    public boolean chooseNickname(String nick) throws RemoteException {
        boolean flag = false;
        try {
            flag = lobbyServer.chooseNickname(nick);
        } catch (ExistentNicknameExcepiton e) {
            flag=false;
        } catch (IllegalNicknameException e) {
            flag=false;
        }
        if (flag) this.nickname = nick;
       return flag;
    }

    /**
     * This method lets the player make a move
     * @param pos : a List of positions
     * @param col : the column of the shelf
     * @return true if the move il legal
     * @throws RemoteException
     */
    public boolean makeMove(List<Position> pos, int col) throws RemoteException, InvalidIdException, InvalidMoveException {
        return this.matchServer.makeMove(pos, col, nickname);
    }

    /**
     * This method lets a player create a game and choose the available player slots
     * @param num : player slots
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void createGame(int num) throws RemoteException, NotBoundException {
        ConnectionInformationRMI c = this.lobbyServer.createGame(num, nickname, this);
        this.connectToMatchServer(c);
    }

    /**
     * This method lets a player join a game
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void joinGame() throws RemoteException, NotBoundException, NoGamesAvailableException {
        ConnectionInformationRMI c = null;
        try {
            c = this.lobbyServer.joinGame(nickname, this);
        } catch (NoGamesAvailableException e) {
            throw new NoGamesAvailableException();
        }
        this.connectToMatchServer(c);
    }

    /**
     * This method connects to the MatchServer using information available in the parameter
     * @param c : a ConnectionInformationRMI object
     * @throws RemoteException
     * @throws NotBoundException
     */
    private void connectToMatchServer(ConnectionInformationRMI c) throws RemoteException, NotBoundException {
        System.out.println("Looking up the registry for MatchServer");
        System.out.println("Name: "+c.getRegistryName()+" Port: "+c.getRegistryPort());
        Registry registry = LocateRegistry.getRegistry(c.getRegistryPort());
        this.matchServer = (RmiServerInterface) registry.lookup(c.getRegistryName());
    }

    /**
     * This method lets the server check if the client is alive
     * @return true
     * @throws RemoteException
     */
    public boolean isAlive() throws RemoteException {return true;}


    /**
     * This method lets the client send a message privately only to someone
     * @param message
     * @param receiver : the one that is supposed to receive the message
     * @throws RemoteException
     */
    public void messageSomeone(String message, String receiver) throws RemoteException{
        this.matchServer.messageSomeone(message, this.nickname, receiver);
    }

    /**
     * This method lets the client send a message to every other client connected to the game
     * @param message
     * @throws RemoteException
     */
    public void messageAll(String message) throws RemoteException{
        this.matchServer.messageAll(message, this.nickname);

    }

    /**
     * This method lets the server ask a client for his nickname
     * @return the nickname of the client
     * @throws RemoteException
     */
    public String name() throws RemoteException{
        return this.nickname;
    }

    /**
     * This method notifies the view that a message has arrived
     * @param message
     * @throws RemoteException
     */
    public void receiveMessage(String message) throws RemoteException{
        this.view.displayChatMessage(message);
    }

}
