package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Card class
 */
public class CardTest {

    Card c = new Card(CardColor.GREEN, 2);

    /**
     * This method tests the getColor method of the Card class
     */
    @org.junit.jupiter.api.Test
    void getColor() {
        assertEquals(CardColor.GREEN, c.color());
    }

    /**
     * This method tests the getSprite method of the Card class
     */
    @org.junit.jupiter.api.Test
    void getSprite() {
        assertEquals(2, c.sprite());
    }
}