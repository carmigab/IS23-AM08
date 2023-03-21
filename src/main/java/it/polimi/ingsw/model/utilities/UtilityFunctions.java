package it.polimi.ingsw.model.utilities;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to contain utility functions that will be used in different part of the program
 */
public class UtilityFunctions {
    /**
     * This method is a private method used by the evaluate method to find the size of a group
     * containing the given card
     * This method use BFS to search for groups and mark the visited cards as empty to avoid
     * double-checking
     *
     * @param shelf  shelf to find groups in
     * @param position position of the given card
     * @return size of the group found
     */
    public static Integer findGroupSize(Shelf shelf, Position position) {
        return findGroupContainingGivenPosition(shelf, position).size();

//        group size
//        Integer groupSize = 0;
//
//        // frontier to store the position to be examined
//        ArrayDeque<Position> frontier = new ArrayDeque<>();
//
//        // color of the current group
//        TileColor color = shelf.getCard(position).getColor();
//
//        // add the given position to the frontier to start the algorithm
//        frontier.add(position);
//
//        // while the frontier is not empty extract the first element, if the card in that
//        // position has the same color of the group add one to group size,
//        // set the corresponding card to empty and add the right, bottom, top and left position
//        // to the queue if not already presents
//        while (!frontier.isEmpty()) {
//            Position extraxtedPosition = frontier.removeFirst();
//            Tile card = shelf.getCard(extraxtedPosition);
//
//            // if the card in the position extracted from the frontier has the same color of the group
//            if (card.getColor().equals(color) && !card.isEmpty()) {
//                groupSize++;
//                // set the card in the extracted position to empty to avoid double-checking
//                card.setEmpty();
//
//                // if the extracted position is in the last column do not execute this part
//                if (extraxtedPosition.x() < AppConstants.COLS_NUMBER - 1) {
//                    frontier.add(new Position(extraxtedPosition.x() + 1, extraxtedPosition.y()));
//                }
//
//                // if the extracted position is in the last row do not execute this part
//                if (extraxtedPosition.y() < AppConstants.ROWS_NUMBER - 1) {
//                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() + 1));
//                }
//
//                // if the extracted position is in the first column do not execute this part
//                if (extraxtedPosition.x() > 0) {
//                    frontier.add(new Position(extraxtedPosition.x() - 1, extraxtedPosition.y()));
//                }
//
//                // if the extracted position is in the first row do not execute this part
//                if (extraxtedPosition.y() >0) {
//                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() - 1));
//                }
//            }
//        }
//
//        return groupSize;
    }

    /**
     * This method return the list of position that are part of the group containing the given position
     *
     * @param shelf  shelf of the player in which search for group
     * @param position starting position of the group
     * @return list of position in the shelf that are part of the group
     */
    public static List<Position> findGroupContainingGivenPosition(Shelf shelf, Position position) {
        List<Position> result = new ArrayList<>();

        // frontier to store the position to be examined
        ArrayDeque<Position> frontier = new ArrayDeque<>();

        // color of the current group
        TileColor color = shelf.getTile(position).getColor();

        // add the given position to the frontier to start the algorithm
        frontier.add(position);

        // while the frontier is not empty extract the first element, if the card in that
        // position has the same color of the group add one to group size,
        // set the corresponding card to empty and add the right, bottom, top and left position
        // to the queue if not already presents
        while (!frontier.isEmpty()) {
            Position extractedPosition = frontier.removeFirst();
            Tile card = shelf.getTile(extractedPosition);

            // if the card in the position extracted from the frontier has the same color of the group
            if (card.getColor().equals(color) && !card.isEmpty()) {
                result.add(extractedPosition);

                // set the card in the extracted position to empty to avoid double-checking
                card.setEmpty();

                frontier.addAll(getAdjacentPositions(extractedPosition));

//                // if the extracted position is in the last column do not execute this part
//                if (extraxtedPosition.x() < AppConstants.COLS_NUMBER - 1) {
//                    frontier.add(new Position(extraxtedPosition.x() + 1, extraxtedPosition.y()));
//                }
//
//                // if the extracted position is in the last row do not execute this part
//                if (extraxtedPosition.y() < AppConstants.ROWS_NUMBER - 1) {
//                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() + 1));
//                }
//
//                // if the extracted position is in the first column do not execute this part
//                if (extraxtedPosition.x() > 0) {
//                    frontier.add(new Position(extraxtedPosition.x() - 1, extraxtedPosition.y()));
//                }
//
//                // if the extracted position is in the first row do not execute this part
//                if (extraxtedPosition.y() >0) {
//                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() - 1));
//                }
            }
        }

        return result;
    }

    /**
     * This method return the adjacent position in the shelf to a given position
     *
     * @param position starting position, the method finds the adjacent to this one
     * @return a list of 2 to 4 positions
     */
    public static List<Position> getAdjacentPositions(Position position) {
        List<Position> adjacentPositions = new ArrayList<>();

        // if the extracted position is in the last column do not execute this part
        if (position.x() < AppConstants.COLS_NUMBER - 1) {
            adjacentPositions.add(new Position(position.x() + 1, position.y()));
        }

        // if the extracted position is in the last row do not execute this part
        if (position.y() < AppConstants.ROWS_NUMBER - 1) {
            adjacentPositions.add(new Position(position.x(), position.y() + 1));
        }

        // if the extracted position is in the first column do not execute this part
        if (position.x() > 0) {
            adjacentPositions.add(new Position(position.x() - 1, position.y()));
        }

        // if the extracted position is in the first row do not execute this part
        if (position.y() >0) {
            adjacentPositions.add(new Position(position.x(), position.y() - 1));
        }

        return adjacentPositions;
    }


    /**
     * This method count how many groups of groupsDim there are inside a bigger group
     *
     * @param group     group to be analyzed
     * @param groupsDim size of desired subgroups
     * @return the number of subgroups of groupsDim dimension presents
     */
    public static int countGroupsOfGivenDim(List<Position> group, int groupsDim) {
        int groupsCounter = 0;

        // adjacency list of all position of the group
        List<List<Position>> adjacencyList = new ArrayList<>();
        for (Position pos : group) {
            List<Position> adjacentPositionSameColor = new ArrayList<>();
            for (Position pos1 : getAdjacentPositions(pos)) {
                if (group.contains(pos1)) adjacentPositionSameColor.add(pos1);
            }
            adjacencyList.add(adjacentPositionSameColor);
        }

        // list of all the subgroups
        List<List<Position>> subGroups = new ArrayList<>();
        int shortestListIndex = findShortestListIndex(adjacencyList);
        while (shortestListIndex >= 0) {
            break;
        }

        return groupsCounter;
    }

    /**
     * This method find the shortest list in a list of lists
     *
     * @param list list of lists
     * @return the index of the shortest list (only if not empty)
     */
    private static int findShortestListIndex(List<List<Position>> list) {
        int shortestIndex = -1;
        int currentIndex = 0;

        for (List<Position> l : list) {
            if (l.size() > 0 && shortestIndex == -1) shortestIndex = currentIndex;

            if (l.size() > list.get(shortestIndex).size()) shortestIndex = currentIndex;
            currentIndex++;
        }

        return shortestIndex;
    }
}
