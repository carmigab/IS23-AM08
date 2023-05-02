package it.polimi.ingsw.model.constants;

/**
 * This class is used to store all the constant values needed by the classes of the game model
 */
public class AppConstants {
    /**
     * shelf rows and cols number
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
    public static int TOTAL_GOALS =  12;

    /**
     * number of cards color, number of cards per color and total number of cards
     */
    public static int TOTAL_COLORS          =   6;
    public static int TOTAL_TILES_PER_COLOR =  22;
    public static int TOTAL_TILES           = 132;

    /**
     * number of common objectives per game
     */
    public static int TOTAL_CG_PER_GAME =   2;

    /**
     * max number of element in the common objective points stack
     */
    public static int MAX_STACK_CG          =   4;

    /**
     * max number of players per game
     */
    public static int MAX_PLAYERS           =   4;

    /**
     * max number of moves which a player can do
     */
    public static int MAX_NUM_OF_MOVES      =   3;

    /**
     *
     */
    public static int TOTAL_POINTS_FOR_PG  =   6;
    /**
     * relative paths of all configuration files
     */
    public static String FILE_CONFIG_PERSONALGOAL = "singleObjectives.json";
    public static String FILE_CONFIG_NGROUPOFSIZEM = "commonGoals1and2.json";
    public static String FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS = "commonGoals5and8and9and10.json";
    public static String FILE_CONFIG_SINGLEOCCURRENCEOFGIVENSHAPE = "commonGoals3and7and11.json";

    //public static String PATH_SAVED_MATCHES = "/home/matteo/Desktop/PROGETTO_IDS/src/main/resources/savedMatches/";
    public static String PATH_SAVED_MATCHES = "src/main/resources/savedMatches/";

}
