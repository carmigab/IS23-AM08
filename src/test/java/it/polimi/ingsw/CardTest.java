package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card c = new Card(CardColor.GREEN, 2);

    @org.junit.jupiter.api.Test
    void getColor() {
        assertEquals(CardColor.GREEN, c.getColor());
    }

    @org.junit.jupiter.api.Test
    void getSprite() {
        assertEquals(2, c.getSprite());
    }
}