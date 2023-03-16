package it.polimi.ingsw.model;

/**
 * this class represent the single card of the game
 */

public class Card {
    /**
     * this attribute is the color of the card;
     */
    private CardColor color;
    /**
     * this attribute is used to show the correct image of the card
     */
    private final int sprite;

    /**
     * this method is the class constructor
     *
     * @param color  : set the attribute "color" to color value
     * @param sprite : set the attribute "sprite" to color value
     */
    public Card(CardColor color, int sprite) {
        this.color = color;
        this.sprite = sprite;
    }

    /**
     * this method is an alternative class constructor that returns a new card, copy of the card passed as parameter
     */
    public Card(Card copy){
        this.color = copy.color;
        this.sprite = copy.sprite;
    }

    /**
     * this method return the value of the attribute color
     * @return a CardColor, the color of the card
     */
    public CardColor getColor() {return color;}

    /**
     * * this method return the value of the attribute sprite
     * @return an int, the sprite of the card
     */
    public int getSprite() {return sprite;}

    /**
     * this method return true if the card is empty
     * @return a boolean
     */
    public boolean isEmpty(){
        return this.color == CardColor.EMPTY;
    }

    /**
     * this method return true if the card is empty
     * @return a boolean
     */
    public boolean isInvalid(){
        return this.color == CardColor.INVALID;
    }

    /**
     * this method set the card color to empty
     */
    public void setEmpty(){this.color = CardColor.EMPTY;}

    /**
     * this method set the card color to invalid
     */
    public void setInvalid(){this.color = CardColor.INVALID;}

    /**
     * this method overrides the method equals. it assumes that two cards are equals if they have the same color;
     * if the parameter isn't a card, the method return false
     * @param obj
     * @return a boolean
     */

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Card)) return false;
        Card c = (Card) obj;
        return this.color == c.color;
    }
}
