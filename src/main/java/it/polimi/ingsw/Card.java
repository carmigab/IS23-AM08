package it.polimi.ingsw;

/**
 * this class represent the single card of the game
 *
 * @param color  this attribute is the color of the card
 * @param sprite this attribute is used to put the image on the card
 */
public record Card(CardColor color, int sprite) {

    /**
     * this method is the class constructor
     *
     * @param color  : set the attribute "color" to color value
     * @param sprite : set the attribute "sprite" to color value
     */
    public Card {
    }

    /**
     * this method return the value of the attribute color
     *
     * @return a cardColor
     */

    @Override
    public CardColor color() {
        return color;
    }

    /**
     * this method return the value of the attribute sprite
     *
     * @return an int
     */

    @Override
    public int sprite() {
        return sprite;
    }
}
