package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

public interface TcpClientInterface extends Runnable {

    public void run();

    public void update(State newState, GameInfo newInfo) throws TimeOutException;

    public void isAlive() throws TimeOutException;

    public void receiveMessage(String message) throws TimeOutException;

    public void setMatchServer(MatchServer matchServer);

    public String name();
}
