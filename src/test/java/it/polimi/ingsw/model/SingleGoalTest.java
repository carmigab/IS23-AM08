package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit test for class SingleGoal
 */
class SingleGoalTest {

    /**
     * this method tests the method getPosition; we created a new position and a new singleObjective, and we verified if
     * getPosition return the same reference of p
     */
    @Test
    void getPosition() {
        Position p = new Position(5,5);
        SingleGoal s= new SingleGoal(p, TileColor.EMPTY);
        assertEquals(p,s.getPosition());
    }

    /**
     * this method tests the method getColor; we created a new singleObjective, and we verified if getColor return the
     */
    @Test
    void getColor() {
        SingleGoal s = new SingleGoal(new Position(5,5), TileColor.EMPTY);
        assertEquals(TileColor.EMPTY, s.getColor());
    }
}