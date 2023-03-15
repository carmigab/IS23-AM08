package it.polimi.ingsw;

/**
 * This class is used to store all the constant values needed by the classes of the game model
 */
public class AppConstants {
    /**
     * library rows and cols number
     */
    public static int ROWS_NUMBER           =   6;
    public static int COLS_NUMBER           =   5;

    /**
     * board dimension
     */
    public static int BOARD_DIMENSION       =   9;

    /**
     * total personal and common objectives number (is the same for personal and common)
     */
    public static int TOTAL_OBJECTIVES      =  12;

    /**
     * number of cards color, number of cards per color and total number of cards
     */
    public static int TOTAL_COLORS          =   6;
    public static int TOTAL_CARDS_PER_COLOR =  22;
    public static int TOTAL_CARDS           = 132;

    /**
     * number of common objectives per game
     */
    public static int TOTAL_CO_PER_GAME     =   2;

    /**
     * max number of element in the common objective points stack
     */
    public static int MAX_STACK_CO          =   4;

    /**
     * max number of players per game
     */
    public static int MAX_PLAYERS           =   4;
}
