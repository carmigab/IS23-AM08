package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DumbComputer extends View{

    private final Integer myPosition;
    private final String gameName;
    private final Random r;
    private boolean hasToBeUpdated;
    private RmiClient client;

    public DumbComputer(Integer myPosition, String nickname, String gameName) {
        this.myPosition = myPosition;
        this.myNickname=nickname;
        this.gameName=gameName;
        this.hasToBeUpdated=true;
        this.iWantToPlay=true;
        this.r=new Random();
        try {
            this.client=new RmiClient(this.myNickname, this, "localhost", ServerConstants.RMI_PORT);
            this.client.connectToMatchServer(this.gameName);
            this.startUpdateThread();
        }catch (RemoteException | NotBoundException | InterruptedException e) {
            System.out.println("Ending life of COM"+(myPosition-1));
        }
    }

    public void update(State newState, GameInfo newGameInfo) {
        this.currentState = newState;
        this.gameInfo = newGameInfo;
        this.hasToBeUpdated=true;
    }

    private void startUpdateThread(){
        new Thread(()->{
            while(iWantToPlay){
                try {
                    Thread.sleep(800);
                    if(this.hasToBeUpdated){
                        this.hasToBeUpdated=false;
                        System.out.println(this.myNickname+": Update received!");
                        if(this.currentState.equals(State.ENDGAME)) iWantToPlay=false;
                        if(gameInfo!=null && iWantToPlay) {
                            if (isMyTurn()) {
                                boolean done = false;
                                System.out.println(this.myNickname + ": My turn now!");
                                while (!done) {
                                    System.out.println(this.myNickname + ": Trying to make a move!");
                                    int numOfTilesSelected = this.r.nextInt(3) + 1;
                                    List<Position> toSend = new ArrayList<>(numOfTilesSelected);
                                    Position start=new Position(this.r.nextInt(9), this.r.nextInt(9));
                                    toSend.add(start);
                                    if(this.r.nextDouble()<0.3){
                                        int random=r.nextInt(4);
                                        switch (random) {
                                            case 0 -> toSend.add(new Position(start.x() + 1, start.y()));
                                            case 1 -> toSend.add(new Position(start.x(), start.y() + 1));
                                            case 2 -> toSend.add(new Position(start.x() - 1, start.y()));
                                            case 3 -> toSend.add(new Position(start.x(), start.y() - 1));
                                        }
                                    }
                                    try {
                                        Thread.sleep(300);
                                        this.client.makeMove(toSend, this.r.nextInt(5));
                                        done = true;
                                    } catch (InvalidNicknameException | InvalidMoveException | ConnectionError | InterruptedException | GameEndedException ignored) {
                                    }
                                }
                                System.out.println(this.myNickname + ": Move done!");
                            }
                        }
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    @Override
    protected void display() {

    }

    /**
     * This method is called by getUserInput to welcome the player
     */
    @Override
    protected void welcome() {

    }

    @Override
    protected void waitForGameStart() {

    }

    @Override
    protected String waitCommand() {
        return null;
    }

    @Override
    protected void parseCommand(String command) {

    }

    @Override
    protected void chooseConnectionType() {

    }

    @Override
    protected void askNickname() {

    }

    @Override
    protected void createOrJoinGame() {

    }

    @Override
    protected boolean askIfWantToPlayAgain() {
        return false;
    }

    @Override
    protected void notifyClose(String message) {

    }

    @Override
    public void displayChatMessage(String message) {

    }
    public RmiClientInterface getRMIClientInterface(){
        return this.client;
    }
}
