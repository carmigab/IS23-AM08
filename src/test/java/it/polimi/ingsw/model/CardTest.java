package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Card class
 */
public class CardTest {



    /**
     * This method tests the getColor method of the Card class
     */
    @Test
    void getColor() {
        Card c = new Card(CardColor.GREEN, 2);
        assertEquals(CardColor.GREEN, c.getColor());
    }

    /**
     * This method tests the getSprite method of the Card class
     */
    @Test
    void getSprite() {
        Card c = new Card(CardColor.GREEN, 2);
        assertEquals(2, c.getSprite());
    }

    @Test
    void isEmpty(){
        Card c = new Card(CardColor.EMPTY, 2);
        assertTrue(c.isEmpty());
        c.setInvalid();
        assertFalse(c.isEmpty());
    }


    @Test
    void isInvalid(){
        Card c = new Card(CardColor.INVALID, 2);
        assertTrue(c.isInvalid());
        c.setEmpty();
        assertFalse(c.isInvalid());
    }

    @Test
    void setEmpty(){
        Card c = new Card(CardColor.INVALID, 2);
        c.setEmpty();
        assertTrue(c.isEmpty());
    }

    @Test
    void setInvalid(){
        Card c = new Card(CardColor.INVALID, 2);
        c.setInvalid();
        assertTrue(c.isInvalid());
    }

    @Test
    void equals(){
        Card c = new Card(CardColor.INVALID, 2);
        String s = new String();
        assertFalse(c.equals(s));
        Card c1 = new Card(CardColor.INVALID, 2);
        assertTrue(c.equals(c1));
        c1.setEmpty();
        assertFalse(c.equals(c1));
    }


}