package it.polimi.ingsw.constants;

/**
 * This class contains the constants used by the CLI
 */
public class ViewConstants {
    /**
     * the length of the nickname
     */
    public static final int MAX_NICKNAME_LENGTH = 30;

    public static final boolean ENABLE_BOARD_AND_SHELF_BORDER = true;

    public static final int SHELF_REPRESENTATION_DIMENSION = 8;

    public static final int CHAT_TIMER = 1000000;

    public static final String REGEX_INPUT_YES_OR_NO = "y|Y|n|N";

    public static final String REGEX_INPUT_SINGLE_MOVE = "^[0-9]+,[0-9]+$";

    public static final String REGEX_INPUT_COLUMN = "^[0-"+(ModelConstants.COLS_NUMBER-1)+"]+$";

    public static final String REGEX_INPUT_CHAT_MESSAGE = "^[A-Za-z0-9+_.-]+: (.+)$";

    //https://www.geeksforgeeks.org/how-to-validate-an-ip-address-using-regular-expressions-in-java/
    public static final String REGEX_INPUT_IP = "|localhost|(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";

    public static final String REGEX_INPUT_PORT = "|default|\\d+";

    public static final String REGEX_INPUT_CONNECTION_TYPE = "rmi|RMI|tcp|TCP";

    public static final String REGEX_INPUT_INTERVAL_OF_PLAYERS = "[2-4]";

    public static final String REGEX_INPUT_ORDER_2TILES = "^[1-3]+,[1-3]+$";

    public static final String REGEX_INPUT_ORDER_3TILES = "^[1-3]+,[1-3]+,[1-3]+$";
}
