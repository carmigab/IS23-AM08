package it.polimi.ingsw.launchers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

public class TrainingLauncher {

    private GameController controller;
    private State currentState;
    private GameInfo currentGameInfo;

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void update(State currentState, GameInfo currentGameInfo){

        System.out.println(currentState);
        System.out.println(currentGameInfo.getCurrentPlayerNickname());

        this.currentState=currentState;
        this.currentGameInfo=currentGameInfo;

    }

    public boolean isGameEnded(){ return this.currentState.equals(State.ENDGAME); }

    public String getCurrentPlayer(){ return this.currentGameInfo.getCurrentPlayerNickname(); }

    public boolean makeMove(List<Integer> positions, int size, int column, String nickname){
        List<Position> positionList=new ArrayList<>(size);
        for(int i=0;i<size;i+=2){
            positionList.add(new Position(positions.get(i), positions.get(i+1)));
        }
        try {
            this.controller.makeMove(positionList,column, nickname);
        } catch (InvalidMoveException e) {
            System.out.println("Invalid Move");
            return false;
        } catch (InvalidNicknameException e) {
            System.out.println("Invalid Nickname");
            return false;
        }
        System.out.println("Move done");
        return true;
    }

    public static void main(String[] args) {

        TrainingLauncher server=new TrainingLauncher();

        GameController controller=new GameController(List.of("Pippo", "Topolino"), 2, server);

        server.setController(controller);

        GatewayServer gatewayServer=new GatewayServer(server);
        gatewayServer.start();

    }

}
