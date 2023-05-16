package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;

import java.util.*;

import static it.polimi.ingsw.utilities.UtilityFunctionsModel.getAdjacentPositions;

public class ColorFitnessPerTile {
    private final HashMap<TileColor , Integer>[][] colorFitnessPerTile;

    public ColorFitnessPerTile() {
        initialize();
        colorFitnessPerTile = new HashMap[ModelConstants.ROWS_NUMBER][ModelConstants.COLS_NUMBER];
    }

    private void initialize() {
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                colorFitnessPerTile[i][j] = new HashMap<>();

                for (TileColor tileColor : TileColor.values()) {
                    colorFitnessPerTile[i][j].put(tileColor, 0);
                }
            }
        }
    }

    public void updateColorFitnessPerTile(GameStateRepresentation gameStateRepresentation) {
        int coefficient = 0;
        int groupSize = 0;
        Set<Position> tilesAtGivenManhattanDistance;
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                if (!gameStateRepresentation.getShelf()[i][j].isEmpty()) {
                    groupSize = UtilityFunctionsModel.findGroupSize(new Shelf(gameStateRepresentation.getShelf()), new Position(i, j));

                    for (int k = 0; k < 9; k++) {
                        tilesAtGivenManhattanDistance = getTilesAtGivenManhattanDistance(k, new Position(i, j), gameStateRepresentation);
                        coefficient = computeCoefficient(k, groupSize);

                        for (Position position : tilesAtGivenManhattanDistance) {
                            colorFitnessPerTile[position.x()][position.y()].put(gameStateRepresentation.getShelf()[i][j].getColor(), coefficient);
                        }
                    }
                }
            }
        }
    }

    private Set<Position> getTilesAtGivenManhattanDistance(int manhattanDistance, Position position, GameStateRepresentation gameStateRepresentation) {
        Set<Position> result = new HashSet<>();
        Set<Position> alreadyVisited = new HashSet<>();

        alreadyVisited.add(position);
        for (int i = 0; i < manhattanDistance - 1; i++) {
            alreadyVisited.addAll(getAdjacentPositionsOfGroup(alreadyVisited.stream().toList()));
        }

        for (Position position1 : alreadyVisited) {
            if (gameStateRepresentation.getShelf()[position1.x()][position1.y()].isEmpty()) {
                result.add(position1);
            }
        }

        return result;
    }

    private Set<Position> getAdjacentPositionsOfGroup(List<Position> positionsList) {
        Set<Position> result = new HashSet<>();
        List<Position> adjacentPositions;

        for (Position position : positionsList) {
            adjacentPositions = getAdjacentPositions(position, false);

            for (Position adjacentPosition : adjacentPositions) {
                if (!positionsList.contains(adjacentPosition)) {
                    result.add(adjacentPosition);
                }
            }
        }

        return result;
    }

    private int computeCoefficient(int manhattanDistance, int groupSize) {
        return (1 / manhattanDistance) * (groupSize < 6 ? groupSize : 0) / 6;
    }
}
