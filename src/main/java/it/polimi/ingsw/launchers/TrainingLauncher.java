package it.polimi.ingsw.launchers;

import it.polimi.ingsw.computer.Action;
import it.polimi.ingsw.computer.Move;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;

import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;
import py4j.GatewayServer;

import java.util.*;
import java.util.stream.Collectors;

public class TrainingLauncher {

    private GameController controller;
    private State currentState;
    private GameInfo currentGameInfo;
    private final Map<Action, Move> moveActionMap;

    public TrainingLauncher() {
        this.moveActionMap=new HashMap<>();
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void update(State currentState, GameInfo currentGameInfo){

        //System.out.println(currentState);
        //System.out.println(currentGameInfo.getCurrentPlayerNickname());

        this.currentState=currentState;
        this.currentGameInfo=currentGameInfo;

    }

    public boolean isGameEnded(){
        return this.currentState.equals(State.ENDGAME);
    }

    public GameInfo getCurrentGameInfo() {
        return currentGameInfo;
    }

    public boolean makeMove(List<Position> positions, int column, String nickname){
        try {
            this.controller.makeMove(positions,column, nickname);
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
