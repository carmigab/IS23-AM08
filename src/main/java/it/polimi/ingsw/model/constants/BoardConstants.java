package it.polimi.ingsw.model.constants;

/**
 *
 */
public final class BoardConstants {

    /**
     * board dimension
     */
    public static final int BOARD_DIMENSION       =   9;

    /**
     * number of cards color, number of cards per color and total number of cards
     */
    public static final int TOTAL_COLORS          =   6;
    public static final int TOTAL_CARDS_PER_COLOR =  22;
    public static final int TOTAL_CARDS           = 132;

    /**
     * number of common objectives per game
     */
    public static final int TOTAL_CO_PER_GAME     =   2;
    /**
     * TODO understand what happens when maven builds the project in .jar
     * paths to the game board configuration files
     */
    public static final String FILE_CONFIG_GAMEBOARD2 = "src/main/config/modelgameBoard2.json";
    public static final String FILE_CONFIG_GAMEBOARD3 = "src/main/config/modelgameBoard3.json";
    public static final String FILE_CONFIG_GAMEBOARD4 = "src/main/config/modelgameBoard4.json";
}
