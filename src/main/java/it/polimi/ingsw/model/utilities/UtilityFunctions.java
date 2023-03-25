package it.polimi.ingsw.model.utilities;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.commonGoals.*;
import it.polimi.ingsw.model.constants.AppConstants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
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
     * This method takes an array of strings in input, and it outputs a file name which consists of all the strings in the array
     * concatenated with "_"
     * @param in players' names
     * @return json file name
     */
    public static String getJSONFileName(List<String> in){
        String ret="";

        for(String s: in){
           ret=ret.concat(s+"_");
        }

        //remove last '_'
        return ret.substring(0,ret.length()-1).concat(".json");
    }

    /**
     * This method receive and integer and initialize and return the corresponding common goal
     * @param goalIndex index of the goal to be instantiated
     * @return the goal corresponding to index
     */
    public static CommonGoal createCommonGoal(int goalIndex) {
        Gson jsonLoader = JsonWithExposeSingleton.getJsonWithExposeSingleton();
        Reader fileReader = null;
        // creating configuration from json file for common goal 1 and 2
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NGROUPOFSIZEM);
        }
        catch(FileNotFoundException e){
            System.out.println("error");
        }
        NGroupsOfSizeMConfiguration nGroupsOfSizeMConfiguration = jsonLoader.fromJson(fileReader, NGroupsOfSizeMConfiguration.class);

        // creating configuration from json file for common goal 5, 8, 9 and 10
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS);
        }
        catch(FileNotFoundException e){
            System.out.println("error");
        }
        NLinesOfAtMostMDifferentColorsConfiguration nLinesOfAtMostMDifferentColorsConfiguration = jsonLoader.fromJson(fileReader, NLinesOfAtMostMDifferentColorsConfiguration.class);

        // creating configuration from json file for common goal 3, 7 and 11
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_SINGLEOCCURRENCEOFGIVENSHAPE);
        }
        catch(FileNotFoundException e){
            System.out.println("error");
        }
        SingleOccurrenceOfGivenShapeConfiguration singleOccurrenceOfGivenShapeConfiguration = jsonLoader.fromJson(fileReader, SingleOccurrenceOfGivenShapeConfiguration.class);

        CommonGoal commonGoal;

        commonGoal = switch (goalIndex) {
            case 0 -> nGroupsOfSizeMConfiguration.getGoalAt(0);
            case 1 -> nGroupsOfSizeMConfiguration.getGoalAt(1);
            case 2 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(0);
            case 3 -> new TwoSquares();
            case 4 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(0);
            case 5 -> new EightTilesOfTheSameColor();
            case 6 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(1);
            case 7 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(1);
            case 8 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(2);
            case 9 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(3);
            case 10 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(2);
            default -> new Ladder();
        };

        return commonGoal;
    }
}
