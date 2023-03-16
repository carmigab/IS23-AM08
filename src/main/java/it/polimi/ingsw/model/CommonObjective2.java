package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayDeque;

/**
 * This class implements the second common objective: four groups made by four cards of the same color
 */
public class CommonObjective2 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        Library copy = new Library(x.getMyLibrary());

        // counter for group of two cards
        int groupNum = 0;

        // foreach card in library look if its part of a group and return the group dimension
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                // select the current card
                Card c = copy.getCard(new Position(j, i));

                // execute the algorithm only if the current card is not empty
                if (!c.isEmpty()) {
                    // find the dimension of the group of which c is part and return its dimension divided by 4
                    groupNum += findGroupSize(copy, new Position(j, i)) / 4;
                }
            }
        }

        return groupNum >= 4;
    }

    /**
     * This method is a private method used by the evaluate method to find the size of a group containing the given card
     * This method use BFS to search for groups and mark the visited cards as empty to avoid double-checking
     *
     * @param library library to find groups in
     * @param position position of the given card
     * @return size of the group found
     */
    private Integer findGroupSize(Library library, Position position) {
        // group size
        Integer groupSize = 0;

        // frontier to store the position to be examined
        ArrayDeque<Position> frontier = new ArrayDeque<>();

        // color of the current group
        CardColor color = library.getCard(position).getColor();

        // add the given position to the frontier to start the algorithm
        frontier.add(position);

        // while the frontier is not empty extract the first element, if the card in that position has the same color of the group add one to
        // group size, set the corresponding card to empty and add the right and bottom position to the queue if not already presents
        while (!frontier.isEmpty()) {
            Position extraxtedPosition = frontier.removeFirst();
            Card card = library.getCard(extraxtedPosition);

            // if the card in the position extracted from the frontier has the same color of the group
            if (card.getColor().equals(color)) {
                groupSize++;

                // if the extracted position is in the last column do not execute this part
                if (extraxtedPosition.x() < AppConstants.COLS_NUMBER - 1) {
                    frontier.add(new Position(extraxtedPosition.x() + 1, extraxtedPosition.y()));
                }

                // if the extracted position is in the last column do not execute this part
                if (extraxtedPosition.y() < AppConstants.ROWS_NUMBER - 1) {
                    frontier.add(new Position(extraxtedPosition.x(), extraxtedPosition.y() + 1));
                }
            }

            // set the card in the extracted position to empty to avoid double-checking
            card.setEmpty();
        }

        return groupSize;
    }
}
