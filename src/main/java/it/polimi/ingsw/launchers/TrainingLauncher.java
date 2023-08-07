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

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void update(State currentState, GameInfo currentGameInfo){

        System.out.println(currentState);
        System.out.println(currentGameInfo.getCurrentPlayerNickname());

        this.currentState=currentState;
        this.currentGameInfo=currentGameInfo;

    }

    public boolean isGameEnded(){
        return this.currentState.equals(State.ENDGAME);
    }

    public GameInfo getCurrentGameInfo() {
        return currentGameInfo;
    }

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

    protected List<Position> getAdj(List<Position> pos){
        List<Position> result = new ArrayList<>();
        if(pos.isEmpty()){
            for (int i = 0; i < ModelConstants.BOARD_DIMENSION; i++) {
                for (int j = 0; j < ModelConstants.BOARD_DIMENSION; j++) {
                    result.add(new Position(i, j));
                }
            }
        }
        else if(pos.size() == 1){
            for(Position p : pos){
                result.add(new Position(p.x(), p.y()+1));
                result.add(new Position(p.x(), p.y()-1));
                result.add(new Position(p.x()+1, p.y()));
                result.add(new Position(p.x()-1, p.y()));
            }
        }
        else{
            if(pos.get(0).x() == pos.get(1).x()){
                result.add(new Position(pos.get(0).x(), Math.min(pos.get(0).y() - 1, pos.get(1).y() - 1)));
                result.add(new Position(pos.get(0).x(), Math.max(pos.get(0).y() + 1, pos.get(1).y() + 1)));
            }
            else{
                result.add(new Position(Math.min(pos.get(0).x() - 1, pos.get(1).x() - 1), pos.get(0).y()));
                result.add(new Position(Math.max(pos.get(0).x() + 1, pos.get(1).x() + 1), pos.get(0).y()));
            }
        }

        return reduceAdjacent(result);
    }

    private List<Position> reduceAdjacent(List<Position> adj){
        return adj.stream().filter(this::checkSinglePosition).collect(Collectors.toList());
    }

    private boolean checkSinglePosition(Position pos){
        if(pos.x() < 0 || pos.x() >= ModelConstants.BOARD_DIMENSION) return false;
        if(pos.y() < 0 || pos.y() >= ModelConstants.BOARD_DIMENSION) return false;
        if(this.currentGameInfo.getGameBoard()[pos.y()] [pos.x()].isEmpty() || this.currentGameInfo.getGameBoard()[pos.y()] [pos.x()].isInvalid()){
            return false;
        }
        return UtilityFunctionsModel.hasFreeAdjacent(this.currentGameInfo.getGameBoard(), pos);
    }

    public Set<Action> getAvailableActions() {
        Set<Action> availableActions = new HashSet<>();

        List<Position> availablePositions = getAdj(new ArrayList<>());

        for (Position position: availablePositions) {
            availableActions.addAll(getSingleTileActions(position));
            availableActions.addAll(getDoubleTileActions(position));
            availableActions.addAll(getTripleTileActions(position));
        }

        return availableActions;
    }

    private Collection<? extends Action> getSingleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 1)) {
                moves.add(new Move(List.of(position), i));
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    private boolean checkColumn(int col, int numTiles){
        if(col < 0 || col >= ModelConstants.COLS_NUMBER) return false;
        List<PlayerInfo>  player = this.currentGameInfo.getPlayerInfosList();
        for(int i=0; i<player.size();i++){
            if(player.get(i).getNickname().equals(currentGameInfo.getCurrentPlayerNickname())){
                return UtilityFunctionsModel.getFreeSpaces(this.currentGameInfo.getPlayerInfosList().get(i).getShelf(), col) >= numTiles;
            }
        }
        return false;
    }


    private Collection<? extends Action> getDoubleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 2)) {
                List<Position> adj = getAdj(List.of(position));

                for (Position adjPosition: adj) {
                    moves.add(new Move(List.of(position, adjPosition), i));
                }
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    private Collection<? extends Action> getTripleTileActions(Position position) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            if (checkColumn(i, 3)) {
                List<Position> adj = getAdj(List.of(position));

                for (Position adjPosition: adj) {
                    List<Position> adj2 = getAdj(List.of(position, adjPosition));

                    for (Position adj2Position: adj2) {
                        moves.add(new Move(List.of(position, adjPosition, adj2Position), i));
                    }
                }
            }
        }

        return moves.stream()
                .map(this::mapMoveToAction)
                .toList();
    }

    private Action mapMoveToAction(Move move) {
        List<Position> positions = move.positions();

        List<TileColor> tileColors = positions.stream().
                map(position -> this.currentGameInfo.getGameBoard()[position.y()][position.x()].getColor())
                .toList();

        return new Action(tileColors, move.column());
    }

    public static void main(String[] args) {

        TrainingLauncher server=new TrainingLauncher();

        GameController controller=new GameController(List.of("Pippo", "Topolino"), 2, server);

        server.setController(controller);

        GatewayServer gatewayServer=new GatewayServer(server);
        gatewayServer.start();

    }

}
