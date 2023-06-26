package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;

/**
 * This enum contains all the information for the correct setup of the clickable component class
 */
public enum ClickableComponentSetup {

    /**
     * This enum contains all the information for the standard setup of the clickable component class
     */
    STANDARD(
            1,
            1,
            0.045,
            0.045,
            0.045,
            0.045,
            0.015,
            0.015,
            0.5),

    /**
     * This enum contains all the information for the setup of the clickable component class for the game board
     */
    GAMEBOARD(
            ModelConstants.BOARD_DIMENSION,
            ModelConstants.BOARD_DIMENSION,
            0.045,
            0.045,
            0.045,
            0.045,
            0.0,
            0.0,
            0.5),

    /**
     * This enum contains all the information for the setup of the clickable component class for the player shelf
     */
    MYSHELF(
            ModelConstants.ROWS_NUMBER,
            ModelConstants.COLS_NUMBER,
            0.097,
            0.097,
            0.054,
            0.11,
            0.027,
            0.019,
            0.4),

    /**
     * This enum contains all the information for the setup of the clickable component class for the common goals
     */
    COMMONGOAL(
            1,
            1,
            0.42,
            0.0,
            0.0,
            0.36,
            0.13,
            0.17,
            0.2),

    /**
     * This enum contains all the information for the setup of the clickable component class for the personal goals
     */
    PERSONALGOAL(
            1,
            1,
            0.097,
            0.097,
            0.054,
            0.11,
            0.027,
            0.019,
            0.3),

    /**
     * This enum contains all the information for the setup of the clickable component class for the selected cards
     */
    MOVELIST(
            1,
            ModelConstants.MAX_NUM_OF_MOVES,
            0.001,
            0.001,
            0.014,
            0.73,
            0.045,
            0.010,
            0.3),

    /**
     * This enum contains all the information for the setup of the clickable component class for the other player shelves
     */
    OTHERSHELF(
            ModelConstants.ROWS_NUMBER,
            ModelConstants.COLS_NUMBER,
            0.097,
            0.097,
            0.054,
            0.11,
            0.027,
            0.019,
            0.15),

    /**
     * This enum contains all the information for the setup of the clickable component class for the player common goals points
     */
    MYPOINTS(
            1,
            ModelConstants.TOTAL_CG_PER_GAME+1,
            0.001,
            0.001,
            0.014,
            0.73,
            0.045,
            0.010,
            0.3),

    /**
     * This enum contains all the information for the setup of the clickable component class for the other player common goals points
     */
    OTHERPOINTS(1,
            ModelConstants.TOTAL_CG_PER_GAME+1,
            0.001,
            0.001,
            0.014,
            0.73,
            0.045,
            0.010,
            0.15);

    /**
     * Attribute containing how many images there are in the x direction
     */
    private final Integer componentSavedImagesX;
    /**
     * Attribute containing how many images there are in the y direction
     */
    private final Integer componentSavedImagesY;
    /**
     * Percentage offset between the edge of the game board and the first tile on the left
     */
    private final Double tileComponentOffsetXLeft;
    /**
     * Percentage offset between the edge of the game board and the first tile on the right
     */
    private final Double tileComponentOffsetXRight;
    /**
     * Percentage offset between the edge of the game board and the first tile from the top
     */
    private final Double tileComponentOffsetYUp;
    /**
     * Percentage offset between the edge of the game board and the first tile from the bottom
     */
    private final Double tileComponentOffsetYDown;
    /**
     * Percentage offset between two tiles in the x direciton
     */
    private final Double tileComponentDistanceX;
    /**
     * Percentage offset between two tiles in the y direction
     */
    private final Double tileComponentDistanceY;
    /**
     * Percantage ratio between the container dimensions (the smaller one) and the size of the game board
     */
    private final Double componentPredefinedRatio;

    /**
     * Constructor of the component
     * @param componentSavedImagesX number of images in the x direction
     * @param componentSavedImagesY number of images in the y direction
     * @param tileComponentOffsetXLeft left offset of the single tile
     * @param tileComponentOffsetXRight right offset of the single tile
     * @param tileComponentOffsetYUp up offset of the single tile
     * @param tileComponentOffsetYDown down offset  of the single tile
     * @param tileComponentDistanceX x distance between internal tiles
     * @param tileComponentDistanceY y distance between internal tiles
     * @param componentPredefinedRatio ratio of the component relative to the screen size
     */
    ClickableComponentSetup(Integer componentSavedImagesX, Integer componentSavedImagesY, Double tileComponentOffsetXLeft, Double tileComponentOffsetXRight, Double tileComponentOffsetYUp, Double tileComponentOffsetYDown, Double tileComponentDistanceX, Double tileComponentDistanceY, Double componentPredefinedRatio){
        this.componentSavedImagesX=componentSavedImagesX;
        this.componentSavedImagesY=componentSavedImagesY;
        this.tileComponentOffsetXLeft=tileComponentOffsetXLeft;
        this.tileComponentOffsetXRight=tileComponentOffsetXRight;
        this.tileComponentOffsetYUp=tileComponentOffsetYUp;
        this.tileComponentOffsetYDown=tileComponentOffsetYDown;
        this.tileComponentDistanceX=tileComponentDistanceX;
        this.tileComponentDistanceY=tileComponentDistanceY;
        this.componentPredefinedRatio=componentPredefinedRatio;
    }

    /**
     * Getter of the component saved images x
     * @return an integer
     */
    public Integer getComponentSavedImagesX() {
        return componentSavedImagesX;
    }

    /**
     * Getter of the component saved images y
     * @return an integer
     */
    public Integer getComponentSavedImagesY() {
        return componentSavedImagesY;
    }

    /**
     * Getter of the component x left offset
     * @return a double
     */
    public Double getTileComponentOffsetXLeft() {
        return tileComponentOffsetXLeft;
    }

    /**
     * Getter of the component x right offset
     * @return a double
     */
    public Double getTileComponentOffsetXRight() {
        return tileComponentOffsetXRight;
    }

    /**
     * Getter of the component y up offset
     * @return a double
     */
    public Double getTileComponentOffsetYUp() {
        return tileComponentOffsetYUp;
    }

    /**
     * Getter of the component y down offset
     * @return a double
     */
    public Double getTileComponentOffsetYDown() {
        return tileComponentOffsetYDown;
    }

    /**
     * Getter of the component x distance
     * @return a double
     */
    public Double getTileComponentDistanceX() {
        return tileComponentDistanceX;
    }

    /**
     * Getter of the component y distance
     * @return a double
     */
    public Double getTileComponentDistanceY() {
        return tileComponentDistanceY;
    }

    /**
     * Getter of the component predefined ratio
     * @return a double
     */
    public Double getComponentPredefinedRatio() {
        return componentPredefinedRatio;
    }
}
