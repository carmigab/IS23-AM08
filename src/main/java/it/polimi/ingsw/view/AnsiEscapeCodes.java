package it.polimi.ingsw.view;

/**
 * This class contains the ANSI escape codes with 24 bit color
 */
public enum AnsiEscapeCodes {
    // bg colors
    /**
     * default odd background color
     */
    DEFAULT_ODD_BACKGROUND("\u001B[48;2;48;48;48m"),

    /**
     * default even background color
     */
    DEFAULT_EVEN_BACKGROUND("\u001B[48;2;41;41;41m"),

    /**
     * default white background color
     */
    WHITE_BACKGROUND("\u001B[48;2;255;255;255m"),

    /**
     * default blue background color
     */
    BLUE_BACKGROUND("\u001B[48;2;66;50;240m"),

    /**
     * default yellow background color
     */
    YELLOW_BACKGROUND("\u001B[48;2;255;237;36m"),

    /**
     * default violet background color
     */
    VIOLET_BACKGROUND("\u001B[48;2;255;36;153m"),

    /**
     * default cyan background color
     */
    CYAN_BACKGROUND("\u001B[48;2;66;237;231m"),

    /**
     * default green background color
     */
    GREEN_BACKGROUND("\u001B[48;2;27;186;9m"),

    /**
     * default empty background color
     */
    EMPTY_BACKGROUND("\u001B[48;2;105;105;105m"),

    /**
     * default board border background color
     */
    BOARD_BORDER_BACKGROUND("\u001B[48;2;64;45;24m"),

    /**
     * default shelf background even color
     */
    EMPTY_SHELF_EVEN("\u001B[48;2;111;78;41m"),

    /**
     * default shelf background odd color
     */
    EMPTY_SHELF_ODD("\u001B[48;2;84;59;31m"),

    // text colors
    /**
     * default text color
     */
    DEFAULT_TEXT("\u001B[38;2;48;48;48m"),

    /**
     * default white text color
     */
    WHITE_TEXT("\u001B[38;2;255;255;255m"),

    /**
     * default blue text color
     */
    BLUE_TEXT("\u001B[38;2;66;50;240m"),

    /**
     * default yellow text color
     */
    YELLOW_TEXT("\u001B[38;2;255;237;36m"),

    /**
     * default violet text color
     */
    VIOLET_TEXT("\u001B[38;2;255;36;153m"),

    /**
     * default cyan text color
     */
    CYAN_TEXT("\u001B[38;2;66;237;231m"),

    /**
     * default green text color
     */
    GREEN_TEXT("\u001B[38;2;27;186;9m"),

    /**
     * default empty text color
     */
    EMPTY_TEXT("\u001B[38;2;105;105;105m"),

    // messages colors
    /**
     * default error message color
     */
    ERROR_MESSAGE("\u001B[38;2;252;53;53m"),

    /**
     * default game message color
     */
    GAME_MESSAGE("\u001B[38;2;255;255;255m"),

    /**
     * default info message color
     */
    INFO_MESSAGE("\u001B[38;2;17;110;58m"),

    /**
     * default chat message color
     */
    CHAT_MESSAGE("\u001B[38;2;0;99;186m"),

    // ending code
    /**
     * default ending code
     */
    ENDING_CODE("\u001B[0m");

    /**
     * This attribute is the ANSI escape code
     */
    private final String code;

    /**
     * This is the constructor of the class
     * @param code is the ANSI escape code
     */
    AnsiEscapeCodes(String code) {
        this.code = code;
    }

    /**
     * This method returns the ANSI escape code
     * @return the ANSI escape code
     */
    public String getCode() {
        return code;
    }
}
