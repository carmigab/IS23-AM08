package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective9
 *
 */
class CommonObjective9Test {

    CommonObjective9 co9 = new CommonObjective9();

    /**
     * This method test the evaluate method of CommonObjective9
     */
    @Test
    void evaluate() {
        Library newLib = new Library();

        // G: 4
        newLib.add(new Card(CardColor.GREEN, 0), 0);
        newLib.add(new Card(CardColor.GREEN, 0), 1);
        newLib.add(new Card(CardColor.GREEN, 0), 2);
        newLib.add(new Card(CardColor.GREEN, 0), 3);

        newLib.add(new Card(CardColor.BLUE, 0), 0);
        newLib.add(new Card(CardColor.BLUE, 0), 1);

        newLib.add(new Card(CardColor.WHITE, 0), 0);
        newLib.add(new Card(CardColor.WHITE, 0), 1);

        newLib.add(new Card(CardColor.VIOLET, 0), 0);
        newLib.add(new Card(CardColor.VIOLET, 0), 1);

        newLib.add(new Card(CardColor.YELLOW, 0), 0);
        newLib.add(new Card(CardColor.YELLOW, 0), 1);

        newLib.add(new Card(CardColor.LIGHT_BLUE, 0), 0);


        assertEquals(co9.evaluate(newLib), false);


        newLib.add(new Card(CardColor.LIGHT_BLUE, 0), 1);
        assertEquals(co9.evaluate(newLib), true);



    }
}