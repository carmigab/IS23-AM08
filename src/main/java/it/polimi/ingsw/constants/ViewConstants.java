package it.polimi.ingsw.constants;

/**
 * This class contains the constants used by the CLI
 */
public class ViewConstants {
    /**
     * the length of the nickname
     */
    public static final int MAX_NICKNAME_LENGTH = 30;

    /**
     * used to add borders to the board and the shelf
     */
    public static final boolean ENABLE_BOARD_AND_SHELF_BORDER = true;

    /**
     * line number of the shelf representation
     */
    public static final int SHELF_REPRESENTATION_DIMENSION = 10;

    /**
     * regex used to validate the yes or no input
     */
    public static final String REGEX_INPUT_YES_OR_NO = "y|Y|n|N";

    /**
     * regex used to validate the single move input
     */
    public static final String REGEX_INPUT_SINGLE_MOVE = "^[0-9]+,[0-9]+$";

    /**
     * regex used to validate the column input
     */
    public static final String REGEX_INPUT_COLUMN = "^[0-"+(ModelConstants.COLS_NUMBER-1)+"]+$";

    /**
     * regex used to validate the chat message input
     */
    public static final String REGEX_INPUT_CHAT_MESSAGE = "^[A-Za-z0-9+_.-]+: (.+)$";

    //https://www.geeksforgeeks.org/how-to-validate-an-ip-address-using-regular-expressions-in-java/
    /**
     * regex used to validate the ip address input
     */
    public static final String REGEX_INPUT_IP = "|localhost|(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";

    /**
     * regex used to validate the port input
     */
    public static final String REGEX_INPUT_PORT = "|default|\\d+";

    /**
     * regex used to validate the connection type input
     */
    public static final String REGEX_INPUT_CONNECTION_TYPE = "rmi|RMI|tcp|TCP";

    /**
     * regex used to validate the number of players input
     */
    public static final String REGEX_INPUT_INTERVAL_OF_PLAYERS = "[2-4]";

    /**
     * regex used to validate the number order 2 tiles input
     */
    public static final String REGEX_INPUT_ORDER_2TILES = "^[1-3]+,[1-3]+$";

    /**
     * regex used to validate the number order 3 tiles input
     */
    public static final String REGEX_INPUT_ORDER_3TILES = "^[1-3]+,[1-3]+,[1-3]+$";

    /**
     * regex used to validate the join game input
     */
    public static final String REGEX_INPUT_JOIN_GAME = "^[0-9]+|r|R";
}
