package it.polimi.ingsw.model.utilities;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
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
     * @param library library to find groups in
     * @param position position of the given card
     * @return size of the group found
     */
    public static Integer findGroupSize(Library library, Position position) {
        return findGroupContainingGivenPosition(library, position).size();

//        group size
//        Integer groupSize = 0;
//
//        // frontier to store the position to be examined
//        ArrayDeque<Position> frontier = new ArrayDeque<>();
//
//        // color of the current group
//        CardColor color = library.getCard(position).getColor();
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
//            Card card = library.getCard(extraxtedPosition);
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
     * @param library library of the player in which search for group
     * @param position starting position of the group
     * @return list of position in the library that are part of the group
     */
    public static List<Position> findGroupContainingGivenPosition(Library library, Position position) {
        List<Position> result = new ArrayList<>();

        // frontier to store the position to be examined
        ArrayDeque<Position> frontier = new ArrayDeque<>();

        // color of the current group
        CardColor color = library.getCard(position).getColor();

        // add the given position to the frontier to start the algorithm
        frontier.add(position);

        // while the frontier is not empty extract the first element, if the card in that
        // position has the same color of the group add one to group size,
        // set the corresponding card to empty and add the right, bottom, top and left position
        // to the queue if not already presents
        while (!frontier.isEmpty()) {
            Position extraxtedPosition = frontier.removeFirst();
            Card card = library.getCard(extraxtedPosition);

            // if the card in the position extracted from the frontier has the same color of the group
            if (card.getColor().equals(color) && !card.isEmpty()) {
                result.add(extraxtedPosition);

                // set the card in the extracted position to empty to avoid double-checking
                card.setEmpty();

                // if the extracted position is in the last column do not execute this part
                if (extraxtedPosition.x() < AppConstants.COLS_NUMBER - 1) {
                    frontier.add(new Position(extraxtedPosition.x() + 1, extraxtedPosition.y()));
                }

                // if the extracted position is in the last row do not execute this part
                if (extraxtedPosition.y() < AppConstants.ROWS_NUMBER - 1) {
                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() + 1));
                }

                // if the extracted position is in the first column do not execute this part
                if (extraxtedPosition.x() > 0) {
                    frontier.add(new Position(extraxtedPosition.x() - 1, extraxtedPosition.y()));
                }

                // if the extracted position is in the first row do not execute this part
                if (extraxtedPosition.y() >0) {
                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() - 1));
                }
            }
        }

        return result;
    }
}
