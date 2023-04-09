package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.client.RmiClientInterface;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

import javax.print.DocFlavor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {

    // This list represents also the playing order
    private List<String> nicknamesList = new ArrayList<>();
    private List<RmiClientInterface> rmiClients = new ArrayList<>();
    private int numPlayers;
    private State state;

    private GameController gameController;


    /**
     * Constructor of the RmiServer class
     * @param numPlayers
     * @throws RemoteException
     */
    public RmiServer(int numPlayers) throws RemoteException {
        super();
        this.numPlayers = numPlayers;
        this.state = State.WAITINGFORPLAYERS;
    }


    // This exists only for debugging purposes
    public void registerPlayer(String nickname, RmiClientInterface client) throws RemoteException{
        nicknamesList.add(nickname);
        rmiClients.add(client);

        this.updateClients(State.WAITINGFORPLAYERS, null);
    }

    /**
     * This method let the current player make a move
     * @param pos
     * @param col
     * @param nickname
     * @return true if all went well
     * @throws RemoteException
     * @throws InvalidIdException
     * @throws InvalidMoveException
     */
    public boolean makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidIdException, InvalidMoveException {
        if(!isMyTurn(nickname)) return false;
        gameController.makeMove(pos, col, nickname);
        return true;
    }

    /**
     * This method checks if a player is the currentPlayer
     * @param nickname
     * @return true if the player with nickname 'nickname' is the current player
     */
    private boolean isMyTurn(String nickname){
        int currPlayer;
        switch (state){
            case TURN0 -> currPlayer = 0;
            case TURN1 -> currPlayer = 1;
            case TURN2 -> currPlayer = 2;
            case TURN3 -> currPlayer = 3;
            default -> {return false;}
        }
        return currPlayer == nicknamesList.indexOf(nickname);
    }


    /**
     * This method lets the lobbyServer add a player and his client
     * and starts the game is no player slots are left
     * @param nickname
     * @param rmiClient
     */
    public void addPlayer(String nickname, RmiClientInterface rmiClient){
        nicknamesList.add(nickname);
        rmiClients.add(rmiClient);

        this.updateClients(State.WAITINGFORPLAYERS, null);
        if (this.getFreeSpaces() == 0) this.startGame();
    }

    /**
     * This method returns the number of free spaces in the server
     * @return the number of free player slots in the server
     */
    public int getFreeSpaces(){
        return numPlayers - nicknamesList.size();
    }


    /**
     * This method creates a new controller and launches a new thread that pings the clients
     * oss: note that the first update to the server is called when the model is created
     */
    public void startGame(){
        this.gameController = new GameController(nicknamesList, numPlayers, this);

        Thread t = new Thread(() -> {
            synchronized (nicknamesList) {
                System.out.println("New Ping Thread starting");
                while (true) {
                    try {
                        this.pingClients();
                        nicknamesList.wait(1000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        t.start();
    }

    /**
     * This method updates the server with the new information from the model
     * @param newState
     * @param newInfo
     */
    public void update(State newState, GameInfo newInfo){
        this.state = newState;
        this.updateClients(newState, newInfo);
    }

    /**
     * This method updates the clients
      * @param newState
     * @param newInfo
     */
    private void updateClients(State newState, GameInfo newInfo){
        Iterator<RmiClientInterface> iter = rmiClients.iterator();
        while (iter.hasNext()) {
            RmiClientInterface client = iter.next();
            try {
                client.update(newState, newInfo);
            } catch (RemoteException e) {
                this.gracefulDisconnection();
                break;
            }
        }

    }

    /**
     * This method signal the clients that someone has crashed
     */
    public void gracefulDisconnection(){
        System.out.println("A client has crashed");
        this.updateClients(State.CLIENTCRASHED, null);
    }

    /**
     * This method check if the clients are alive
     */
    private void pingClients() throws RemoteException {
        System.out.println("checking if RmiClient clients are alive");
        Iterator<RmiClientInterface> iter = rmiClients.iterator();
        while (iter.hasNext()) {
            RmiClientInterface client = iter.next();
            try {
                client.isAlive();
            } catch (RemoteException e) {
                this.gracefulDisconnection();
                throw new RemoteException();
            }
        }
    }


}
