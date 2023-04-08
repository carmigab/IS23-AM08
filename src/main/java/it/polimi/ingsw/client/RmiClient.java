package it.polimi.ingsw.client;

import it.polimi.ingsw.dummies.FakeView;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.RMILobbyServerInterface;
import it.polimi.ingsw.server.RmiServer;
import it.polimi.ingsw.server.RmiServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiClient extends Client implements RmiClientInterface{
    private String nickname;

    private RmiServerInterface matchServer;
    private RMILobbyServerInterface lobbyServer;

    // Here we will use an observer or the true view
    private FakeView view;


    public RmiClient(String nickname, FakeView fV) throws RemoteException {
        super();
        this.view = fV;
        this.nickname = nickname;
    }


    @Override
    public void update(State newState, GameInfo newInfo) throws RemoteException {
        this.view.update(newState, newInfo);
    }


    // To do
    public boolean chooseNickname(String nick) throws RemoteException {
        // provvisory (to become lobbyServer)
        matchServer.registerPlayer(nick, this);

        return true;
    }

    // To do
    public boolean makeMove(List<Position> pos, int col){
        return true;
    }

    public void setLobbyServerServer(RMILobbyServerInterface server){
        this.lobbyServer = server;
    }

    // to delete
    public void setMatchServer(RmiServerInterface server){
        this.matchServer = server;
    }
    // To do
    public void createGame(int num){}

    // To do
    public void joinGame(){}

    // returns true
    public boolean isAlive() throws RemoteException {return true;}


}
