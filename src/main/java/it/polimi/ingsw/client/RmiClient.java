package it.polimi.ingsw.Client;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class RmiClient implements RmiClientInterface{
    private String nickname;

    private Registry matchServerRegistry;
    private Registry lobbyServerRegistry;

    // Here we will use an observer or the true view
    private FakeView fakeView;


    public RmiClient(String nickname, FakeView fV){
        this.fakeView = fV;
        this.nickname = nickname;
    }


    @Override
    public void update(State newState, GameInfo newInfo) throws RemoteException {

    }


}
