package it.polimi.ingsw;

/**
 * this class represent the single card of the game
 */
public class Card {

    /**
     * this attribute is the color of the card
     */
    private final CardColor color;

    /**
     * this attribute is used to put the image on the card
     */
    private final int sprite;

    /**
     * this method is the class constructor
     * @param color : set the attribute "color" to color value
     * @param sprite : set the attribute "sprite" to color value
     */
    public Card(CardColor color, int sprite) {
        this.color = color;
        this.sprite = sprite;
    }

    /**
     * this method return the value of the attribute color
     * @return a cardColor
     */

    public CardColor getColor() {
        return color;
    }

    /**
     * this method return the value of the attribute sprite
     * @return an int
     */

    public int getSprite() {
        return sprite;
    }
}
