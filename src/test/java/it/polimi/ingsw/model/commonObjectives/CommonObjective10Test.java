package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective10
 *
 */
class CommonObjective10Test {

    CommonObjective10 co10 = new CommonObjective10();

    /**
     * This method test the evaluate method of CommonObjective10
     */
    @Test
    void evaluate() {
        Library newLib = new Library();

        // G: 4
        newLib.add(new Card(CardColor.GREEN, 0), 0);
        newLib.add(new Card(CardColor.GREEN, 0), 0);
        newLib.add(new Card(CardColor.GREEN, 0), 0);
        newLib.add(new Card(CardColor.GREEN, 0), 0);

        newLib.add(new Card(CardColor.BLUE, 0), 1);
        newLib.add(new Card(CardColor.BLUE, 0), 1);

        newLib.add(new Card(CardColor.WHITE, 0), 2);
        newLib.add(new Card(CardColor.WHITE, 0), 2);

        newLib.add(new Card(CardColor.VIOLET, 0), 3);
        newLib.add(new Card(CardColor.VIOLET, 0), 3);

        newLib.add(new Card(CardColor.YELLOW, 0), 4);


        assertEquals(co10.evaluate(newLib), false);


        newLib.add(new Card(CardColor.YELLOW, 0), 4);
        assertEquals(co10.evaluate(newLib), true);


    }
}