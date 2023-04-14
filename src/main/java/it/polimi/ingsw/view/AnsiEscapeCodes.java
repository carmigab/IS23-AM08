package it.polimi.ingsw.view;

/**
 * This class contains the ANSI escape codes with 24 bit color
 */
public enum AnsiEscapeCodes {
    // colors
    DEFAULT_BACKGROUND("\u001B[48;2;48;48;48m"),
    WHITE_BACKGROUND("\u001B[48;2;255;255;255m"),
    BLUE_BACKGROUND("\u001B[48;2;66;50;240m"),
    YELLOW_BACKGROUND("\u001B[48;2;255;237;36m"),
    VIOLET_BACKGROUND("\u001B[48;2;255;36;153m"),
    CYAN_BACKGROUND("\u001B[48;2;66;237;231m"),
    GREEN_BACKGROUND("\u001B[48;2;27;186;9m"),
    EMPTY_BACKGROUND("\u001B[48;2;105;105;105m"),
    SHELF_COLOR("\u001B[48;2;173;89;5m"),

    // messages colors
    ERROR_MESSAGE("\u001B[38;2;252;53;53m"),
    GAME_MESSAGE("\u001B[38;2;255;255;255m"),
    REQUEST_MESSAGE("\u001B[38;2;17;87;58m"),
    CHAT_MESSAGE("\u001B[38;2;0;99;186m"),

    // ending code
    ENDING_CODE("\u001B[0m ");

    private final String code;

    AnsiEscapeCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}