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
        // you cannot say assertEquals(p,s.getPosition()); since the position p is copied in s by creating a new object
        assertEquals(p.x(),s.getPosition().x());
        assertEquals(p.y(),s.getPosition().y());
    }

    @Test
    void getColor() {
        SingleGoal s = new SingleGoal(new Position(5,5), TileColor.EMPTY);
        assertEquals(TileColor.EMPTY, s.getColor());
    }
}