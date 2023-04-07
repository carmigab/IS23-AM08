package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RmiServer implements RmiServerInterface{

    // This list represents also the playing order
    private List<String> nicknamesList = new ArrayList<>();
    private List<RmiClient> rmiClients = new ArrayList<>();
    private int numPlayers;
    private State state;
    private GameController gameController;


    public RmiServer(int numPlayers){
        this.numPlayers = numPlayers;
        this.state = State.WAITINGFORPLAYERS;
    }




    public boolean makeMove(List<Position> pos, int col, String nickname) throws RemoteException{
        return true;
    }

    // This exists only for debugging purposes
    public void registerPlayer(String nickName, RmiClient client) throws RemoteException{
        nicknamesList.add(nickName);
        rmiClients.add(client);
    }

}
