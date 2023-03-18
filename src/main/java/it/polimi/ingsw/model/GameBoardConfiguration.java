package it.polimi.ingsw.model;

/**
 * This class is just a utility used for storing the loaded data from the config file
 */
public final class GameBoardConfiguration {

    /**
     * This attribute stores the valid positions read from the config file
     */
    private Integer[][] validPositions;
    /**
     * This attribute stores the point stack read from the config file
     */
    private Integer[] pointStack;

    /**
     * Getter of the valid positions
     * @return matrix of valid positions
     */
    public Integer[][] getValidPositions(){
        return this.validPositions;
    }

    /**
     * Getter of the point stack
     * @return array of the point stack
     */
    public Integer[] getPointStack(){
        return this.pointStack;
    }
}
