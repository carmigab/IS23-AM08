package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;

public class CommonObjective11 extends CommonObjective{
    /**
     * This method finds the cross in the library.
     * It keeps storage of 5 pointers to the library in a cross, and it checks every possible cross that could be present in it
     * @param x library of the player
     * @return true if it found the cross
     */
    @Override
    public boolean evaluate(Library x) {
        int ini_x0=0; int ini_x1=2; int ini_x2=1; int ini_x3=0; int ini_x4=2;
        int ini_y0=0; int ini_y1=0; int ini_y2=1; int ini_y3=2; int ini_y4=2;

        int x0,x1,x2,x3,x4,y0,y1,y2,y3,y4;

        for(; ini_y3< AppConstants.ROWS_NUMBER; ini_y0++,ini_y1++,ini_y2++,ini_y3++,ini_y4++){

            x0=ini_x0;x1=ini_x1;x2=ini_x2;x3=ini_x3;x4=ini_x4;
            y0=ini_y0;y1=ini_y1;y2=ini_y2;y3=ini_y3;y4=ini_y4;

            for(; x1< AppConstants.COLS_NUMBER; x0++,x1++,x2++,x3++,x4++){
                Card c=x.getCard(new Position(x0,y0));
                if(!c.isEmpty()) {
                    if (sameColor(c,x.getCard(new Position(x1,y1)))) {
                        if (sameColor(c,x.getCard(new Position(x2,y2)))) {
                            if (sameColor(c,x.getCard(new Position(x3,y3)))) {
                                if (sameColor(c,x.getCard(new Position(x3,y3)))) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        }
        return false;
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
