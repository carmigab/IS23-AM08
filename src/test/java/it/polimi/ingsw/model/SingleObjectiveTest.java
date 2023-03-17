package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit test for class SingleObjective
 */
class SingleObjectiveTest {

    /**
     * this method tests the method getPosition; we created a new position and a new singleObjective and we verified if
     * getPosition return the same reference of p
     */
    @Test
    void getPosition() {
        Position p = new Position(5,5);
        SingleObjective s = new SingleObjective(p, CardColor.EMPTY);
        assertEquals(p,s.getPosition());
    }

    @Test
    void getColor() {
        SingleObjective s = new SingleObjective(new Position(5,5), CardColor.EMPTY);
        assertEquals(CardColor.EMPTY, s.getColor());
    }
}