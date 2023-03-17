package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;

/**
 * This class implements the first common objective: six group of two cards of the same color (different groups can have different colors)
 */
public class CommonObjective1 extends CommonObjective{

    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        Library copy = new Library(x);

        // counter for group of two cards
        int groupNum = 0;

        // create a copy of the library to be modified

        // foreach Card in library look at the card on the right and bottom to check if they have the same color
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                // c1 is the card being examined
                Card c1 = copy.getCard(new Position(j, i));

                // execute this only when c1 is not in the last column
                if (j < AppConstants.COLS_NUMBER - 1) {
                    // c2 is the card on the right of c1
                    Card c2 = copy.getCard(new Position(j + 1, i));

                    // check if c2 has the same color of c1
                    if (!c1.isEmpty() && sameColor(c1, c2)) {
                        groupNum++;
                        // set c1 and c2 to empty to not examine them twice
                        c1.setEmpty();
                        c2.setEmpty();
                    }
                }

                // execute this only when c1 is not in the last row
                if (i < AppConstants.ROWS_NUMBER - 1) {
                    // c3 is the card at the bottom of c1
                    Card c3 = copy.getCard(new Position(j, i + 1));

                    // check if c3 has the same color of c1
                    if (!c1.isEmpty() && sameColor(c1, c3)) {
                        groupNum++;
                        // set c1 and c3 to empty to not examine them twice
                        c1.setEmpty();
                        c3.setEmpty();
                    }
                }
            }
        }

        return groupNum >= 6;
    }

    /**
     * This is a private method used by the evaluate method to check if two card have the same color
     *
     * @param c1 the first card
     * @param c2 the second card
     * @return true if c1 has the same color of c2
     */
    private boolean sameColor (Card c1, Card c2) {
        return c1.getColor().equals(c2.getColor());
    }
}
