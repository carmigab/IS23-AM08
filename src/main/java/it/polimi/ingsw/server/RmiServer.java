package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.client.RmiClientInterface;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

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

    // To do
    public boolean makeMove(List<Position> pos, int col, String nickname) throws RemoteException{
        return true;
    }

    // To do
    private boolean isMyTurn(String nickname){
        return true;
    }

    // To do
    public void addPlayer(String nickname, RmiClientInterface rmiClient){
        nicknamesList.add(nickname);
        rmiClients.add(rmiClient);

        this.updateClients(State.WAITINGFORPLAYERS, null);
    }

    // To do
    public int getFreeSpaces(){
        return 0;
    }

    // To do
    private void startGame(){}

    // To do
    public void update(State newState, GameInfo newInfo){

        this.updateClients(newState, newInfo);
    }

    // To be made private
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


    public void gracefulDisconnection(){}

    // To do
    private void pingClients(){}


}
