package it.polimi.ingsw.constants;

/**
 * This class is used to store all the constant values needed by the classes of the game model
 */
public class ModelConstants {
    /**
     * shelf rows number
     */
    public static int ROWS_NUMBER           =   6;

    /**
     * shelf cols number
     */
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

    /**
     * number of common objectives per game
     */
    public static int TOTAL_CG_PER_GAME =   2;

    /**
     * max number of players per game
     */
    public static int MAX_PLAYERS           =   4;

    /**
     * max number of moves which a player can do
     */
    public static int MAX_NUM_OF_MOVES      =   3;

    /**
     * number of single goal for each personal goal
     */
    public static int SINGLE_GOALS_NUMBER = 6;

    /**
     * relative paths for configuration file
     */
    public static String FILE_CONFIG_PERSONALGOAL = "singleObjectives.json";

    /**
     * relative paths for configuration file
     */
    public static String FILE_CONFIG_NGROUPOFSIZEM = "commonGoals1and2.json";

    /**
     * relative paths for configuration file
     */
    public static String FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS = "commonGoals5and8and9and10.json";

    /**
     * relative paths for configuration file
     */
    public static String FILE_CONFIG_SINGLEOCCURRENCEOFGIVENSHAPE = "commonGoals3and7and11.json";

    /**
     * relative paths to save the matches
     */
    public static String PATH_SAVED_MATCHES = "./savedMatches/";

}
