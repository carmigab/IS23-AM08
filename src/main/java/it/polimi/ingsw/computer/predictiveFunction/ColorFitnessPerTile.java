package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;
import javafx.geometry.Pos;

import java.util.*;

import static it.polimi.ingsw.utilities.UtilityFunctionsModel.getAdjacentPositions;

public class ColorFitnessPerTile {
    private final Map<TileColor , Double>[][] colorFitnessPerTile;

    public ColorFitnessPerTile() {
        colorFitnessPerTile = new Map[ModelConstants.ROWS_NUMBER][ModelConstants.COLS_NUMBER];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                colorFitnessPerTile[i][j] = new HashMap<>();

                for (TileColor tileColor : TileColor.values()) {
                    if (tileColor != TileColor.EMPTY && tileColor != TileColor.INVALID) {
                        colorFitnessPerTile[i][j].put(tileColor, 0.0);
                    }
                }
            }
        }
    }

    public void updateColorFitnessPerTile(GameStateRepresentation gameStateRepresentation) {
        double coefficient;
        int groupSize;
        TileColor singleGoalColor;
        List<Position> tilesAtGivenManhattanDistance;
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                singleGoalColor = null;

                for (SingleGoal singleGoal : gameStateRepresentation.getPersonalGoal()) {
                    if (singleGoal.getPosition().equals(new Position(j, i))) {
                        singleGoalColor = singleGoal.getColor();
                        break;
                    }
                }

                if (singleGoalColor != null && gameStateRepresentation.getShelf()[i][j].isEmpty()) {
                    colorFitnessPerTile[i][j].put(singleGoalColor, colorFitnessPerTile[i][j].get(singleGoalColor) + 1);

                    Tile[][] shelf = gameStateRepresentation.getShelf();
                    shelf[i][j] = new Tile(singleGoalColor, 0);

                    updateCoefficients(gameStateRepresentation, i, j, singleGoalColor);
                }
                else if (!gameStateRepresentation.getShelf()[i][j].isEmpty()) {
                    updateCoefficients(gameStateRepresentation, i, j, gameStateRepresentation.getShelf()[i][j].getColor());
                }
            }
        }
    }

    private void updateCoefficients(GameStateRepresentation gameStateRepresentation, int i, int j, TileColor color) {
        int groupSize;
        List<Position> tilesAtGivenManhattanDistance;
        double coefficient;
        groupSize = UtilityFunctionsModel.findGroupSize(new Shelf(gameStateRepresentation.getShelf()), new Position(j, i));

        for (int k = 1; k < 9; k++) {
            tilesAtGivenManhattanDistance = getTilesAtGivenManhattanDistance(k, new Position(j, i), gameStateRepresentation);
            coefficient = computeCoefficient(k, groupSize);

            for (Position position : tilesAtGivenManhattanDistance) {
                if (gameStateRepresentation.getShelf()[position.y()][position.x()].isEmpty()) {
                    colorFitnessPerTile[position.y()][position.x()].put(color,
                            colorFitnessPerTile[position.y()][position.x()].get(color) + coefficient);
                }
            }
        }
    }

    private List<Position> getTilesAtGivenManhattanDistance(int manhattanDistance, Position position, GameStateRepresentation gameStateRepresentation) {
        List<Position> alreadyVisited = new ArrayList<>();

        alreadyVisited.add(position);
        for (int i = 0; i < manhattanDistance - 1; i++) {
            for (Position pos : getAdjacentPositionsOfGroup (alreadyVisited)) {
                if (!alreadyVisited.contains(pos)) {
                    alreadyVisited.add(pos);
                }
            }
        }

        return new ArrayList<>(getAdjacentPositionsOfGroup(alreadyVisited));
    }

    private List<Position> getAdjacentPositionsOfGroup(List<Position> positionsList) {
        List<Position> result = new ArrayList<>();
        List<Position> adjacentPositions;

        for (Position position : positionsList) {
            adjacentPositions = getAdjacentPositions(position, false);

            for (Position adjacentPosition : adjacentPositions) {
                if (!positionsList.contains(adjacentPosition) && !result.contains(adjacentPosition)) {
                    result.add(adjacentPosition);
                }
            }
        }

        return result;
    }

    public double computeCoefficient(int manhattanDistance, int groupSize) {
        return ((1 / (double) manhattanDistance) * (groupSize < 6 ? (double) groupSize : 0)) / 6;
    }

    public double evaluateAction(Action action, Tile[][] shelf) {
        double result = 0;

        for (TileColor tileColor : action.getTiles()) {
            result += (Math.log((Math.E * (double) action.getTiles().size() / 3))) * colorFitnessPerTile[findFirstFreeSpaceInGivenColumn(action.getColumn(), shelf)][action.getColumn()].get(tileColor);

            for (TileColor tileColor1 : TileColor.values()) {
                if (tileColor1 != tileColor && tileColor1 != TileColor.EMPTY && tileColor1 != TileColor.INVALID) {
                    result -= colorFitnessPerTile[findFirstFreeSpaceInGivenColumn(action.getColumn(), shelf)][action.getColumn()].get(tileColor1);
                }
            }
        }

        return result;
    }

    public int findFirstFreeSpaceInGivenColumn(int column, Tile[][] shelf) {
        int result = ModelConstants.ROWS_NUMBER - 1;
        while(result >= 0 && !shelf[result][column].isEmpty()) {
            result--;
        }

        return result;
    }

    public Map<TileColor, Double>[][] getColorFitnessPerTile() {
        return colorFitnessPerTile;
    }
}
