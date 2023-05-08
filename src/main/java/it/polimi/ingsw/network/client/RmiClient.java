package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.clientLocks.Lock;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.server.RmiServerInterface;
import it.polimi.ingsw.network.server.RMILobbyServerInterface;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.*;
import it.polimi.ingsw.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiClient extends UnicastRemoteObject implements Client, RmiClientInterface{
    /**
     * This attribute represents the nickname of the player
     */
    private String nickname;

    /**
     * This attribute is the rmi reference to the matchServer
     */
    private RmiServerInterface matchServer;

    /**
     * This attribute is the rmi reference to the lobbyServer
     */
    private RMILobbyServerInterface lobbyServer;

    /**
     * This attribute is the registry of the lobby server
     */
    private Registry lobbyRegistry;

    /**
     * This attribute is the View
     */
    private View view;

    /**
     * This attribute is a lock useful for synchronization
     */
    private Object lock = new Lock();
    /**
     * If this flag is true the client is online
     */
    private boolean isClientOnline = true;

    /**
     * If this flag is true the client has to ping the server
     */
    private boolean toPing = true;

    /**
     * If this flag is true the client is silent
     */
    private boolean mute = false;

    /**
     * If this flag is true the client only prints essential messages
     */
    private boolean essential = true;


    /**
     * This method is the constructor of RmiClient
     * @param nickname
     * @param fV : this will become the true view
     * @param ipToConnect ip of the server to connect to
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RmiClient(String nickname, View fV, String ipToConnect, Integer lobbyPort) throws NotBoundException, InterruptedException, RemoteException {
        super();
        this.view = fV;
        this.nickname = nickname;

        // with this command we set a timeout for a rmi method invocation
        int timeout = ServerConstants.PING_TIME + ServerConstants.TCP_WAIT_TIME + 1000;
        System.getProperties().setProperty("sun.rmi.transport.tcp.responseTimeout", String.valueOf(timeout));

        //System.setProperty("java.rmi.server.hostname", "192.168.43.54");
        this.connectToLobbyServer(ipToConnect, lobbyPort);


    }

    /**
     * This method looks up the registry of the lobby server
     * If it doesn't found it he waits for 5 second
     * @throws InterruptedException
     */
    private void connectToLobbyServer(String ipToConnect, Integer lobbyPort) throws InterruptedException {
        while(true) {
            try {
                if (!mute && !essential) System.out.println("Looking up the registry for LobbyServer at "+ipToConnect+":"+lobbyPort);
                // swap 'localhost' with the server ip when trying to connect with two different machines
                this.lobbyRegistry = LocateRegistry.getRegistry(ipToConnect, lobbyPort);
                this.lobbyServer = (RMILobbyServerInterface) this.lobbyRegistry.lookup(ServerConstants.LOBBY_SERVER);
                break;
            } catch (Exception e) {
                if (!mute) System.out.println("Registry not found");
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
        if (newState == State.GRACEFULDISCONNECTION) this.gracefulDisconnection(true);
        else if (newState == State.GAMEABORTED) this.gracefulDisconnection(false);
        else this.view.update(newState, newInfo);
    }

    /**
     * This method lets the player choose his nickname
     * @param nick: the nickname of the player
     * @return true if nickname is available
     * @throws ConnectionError
     */
    @Override
    public boolean chooseNickname(String nick) throws ConnectionError {
        boolean flag = false;
        try {
            flag = lobbyServer.chooseNickname(nick);
        } catch (ExistentNicknameException | IllegalNicknameException e) {
            flag = false;
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from chooseNickname");
            //e.printStackTrace();
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }

        if (flag) this.nickname = nick;
        return flag;
    }

    /**
     * This method lets the player make a move
     * @param pos : a List of positions
     * @param col : the column of the shelf
     * @throws InvalidNicknameException
     * @throws InvalidMoveException
     * @throws ConnectionError
     * @throws GameEndedException
     */
    public void makeMove(List<Position> pos, int col) throws InvalidNicknameException, InvalidMoveException, ConnectionError, GameEndedException {
        try {
            this.matchServer.makeMove(pos, col, nickname);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from makeMove");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }
    }

    /**
     * This method lets a player create a game and choose the available player slots
     * @param num : player slots
     * @throws NonExistentNicknameException
     * @throws AlreadyInGameException
     * @throws ConnectionError
     */
    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        try {
            String matchServerName = this.lobbyServer.createGame(num, nickname, this);
            this.connectToMatchServer(matchServerName);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from createGame");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        } catch (NotBoundException e) {
            if (!mute && !essential) System.out.println("Trying to lock up an unbound registry");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }
    }

    @Override
    public void createGameWithComputer(int num) throws NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        try {
            String matchServerName = this.lobbyServer.createGameWithComputer(num, nickname, this);
            this.connectToMatchServer(matchServerName);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from createGameWithComputer");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        } catch (NotBoundException e) {
            if (!mute && !essential) System.out.println("Trying to lock up an unbound registry");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }
    }

    /**
     * This method lets a player join a game
     * @throws NoGamesAvailableException
     * @throws NonExistentNicknameException
     * @throws AlreadyInGameException
     * @throws ConnectionError
     */
    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        try {
            String matchServerName = this.lobbyServer.joinGame(nickname, this);
            this.connectToMatchServer(matchServerName);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from joinGame");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        } catch (NotBoundException e) {
            if (!mute && !essential) System.out.println("Trying to lock up an unbound registry");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }

    }

    /**
     * This method connects the client to the MatchServer using information available in the parameter
     * it also starts a thread that pings the server
     * @param matchServerName : name of the server to connect
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void connectToMatchServer(String matchServerName) throws RemoteException, NotBoundException {
        this.matchServer = (RmiServerInterface) this.lobbyRegistry.lookup(matchServerName);

        // new thread to ping server
        this.createPingThread();
    }

    /**
     * This method creates a Ping thread that pings the server
     */
    private void createPingThread(){
        if (!mute && !essential) System.out.println("New Ping Thread starting");
        Thread t = new Thread(() -> {
            synchronized (lock) {
                while (toPing) {
                    try {
                        if (!mute && !essential) System.out.println("PING");
                        this.pingServer();
                        lock.wait(ServerConstants.PING_TIME);
                    } catch (InterruptedException e) {
                        if (!mute && !essential) System.out.println("Interrupted exception from PingThread");
                        this.gracefulDisconnection(true);
                    } catch (RemoteException e) {
                        if (!mute && !essential) System.out.println("Remote exception from PingThread");
                        this.gracefulDisconnection(true);
                        break;
                    }
                }
            }
        });
        t.start();
    }

    /**
     * This method lets the server check if the client is alive
     * @throws RemoteException
     */
    public void isAlive() throws RemoteException {}

    /**
     * This method lets the server ask a client for his nickname
     * @return the nickname of the client
     * @throws RemoteException
     */
    public String name() throws RemoteException{
        return this.nickname;
    }

    /**
     * This method lets the client send a message privately to someone
     * @param message: the message
     * @param receiver : the one that is supposed to receive the message
     * @throws ConnectionError
     */
    public void messageSomeone(String message, String receiver) throws ConnectionError {
        try {
            this.matchServer.messageSomeone(message, this.nickname, receiver);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from chat");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }
    }

    /**
     * This method lets the client send a message to every other client connected to the game
     * @param message: the message
     * @throws ConnectionError
     */
    public void messageAll(String message) throws ConnectionError {
        try {
            this.matchServer.messageAll(message, this.nickname);
        } catch (RemoteException e) {
            if (!mute && !essential) System.out.println("Remote exception from chat");
            this.gracefulDisconnection(true);
            throw new ConnectionError();
        }

    }

    /**
     * This method notifies the view that a chat message has arrived
     * @param message: the message
     * @throws RemoteException
     */
    public void receiveMessage(String message) throws RemoteException {
        this.view.displayChatMessage(message);
    }

    /**
     * This method pings the server
     * @throws RemoteException
     */
    private void pingServer() throws RemoteException {
        this.matchServer.isAlive();
        if (!mute && !essential) System.out.println("PONG");
    }

    /**
     * This method manages the disconnection by setting toPing to false and updating the view
     * @param connectionError: boolean that indicates if an error occurred
     */
    private synchronized void gracefulDisconnection(boolean connectionError) {
        if (isClientOnline) {
            if (connectionError && !mute && essential) System.out.println("Connection error");
            if (!connectionError && !mute) System.out.println("Game Aborted");
            if (!mute) System.out.println("Initializing graceful disconnection");
            if (!mute && !essential) System.out.println("Terminating Ping Thread");
            this.toPing = false;
            this.isClientOnline = false;
            view.update(State.GRACEFULDISCONNECTION, null);
        }
    }

}
