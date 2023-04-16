package it.polimi.ingsw.view;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.server.exceptions.NonExistentNicknameException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to represent the CLI view of the game
 */
public class CLI extends View{

    /**
     * This attribute is used to synchronize the visual update of the view
     */
    private final Object displayLock = new Object();

    /**
     * This attribute is used to set a timeout for the user input to avoid deadlocks
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Scanner used to read user input
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * This method is called by getUserInput to wait for other players to join the game
     */
    @Override
    protected void waitForGameStart() {
        printMessage("Waiting for other players to join the game...", AnsiEscapeCodes.INFO_MESSAGE);
        while (this.currentState == State.WAITINGFORPLAYERS) {
            try {
                wait(1000);
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * This method is called by update to display the game
     */
    //TODO: implement this method
    @Override
    protected void display() {
        synchronized (displayLock) {
            // if gameInfo is null the game has not started yet, so we don't need to print nothing
            if (this.gameInfo == null) {
                return;
            }

            printOtherPlayersShelf();
            printBoard();
            printCommonGoals(gameInfo.getCommonGoalsCreated().get(0), gameInfo.getCommonGoalsCreated().get(1));
            printMyShelf();
        }
    }

    /**
     * This method is called by display to print the shelf of the other players
     */
    private void printOtherPlayersShelf() {
        printMessage("Other players' shelf:", AnsiEscapeCodes.GAME_MESSAGE);
        for (PlayerInfo playerInfo : gameInfo.getPlayerInfosList()) {
            if (!playerInfo.getNickname().equals(this.myNickname)) {
                printMessage(playerInfo.getNickname() + "'s shelf:", AnsiEscapeCodes.GAME_MESSAGE);
                printBoardOrShelf(AppConstants.ROWS_NUMBER, AppConstants.COLS_NUMBER, playerInfo.getShelf());
                printMessage("   Common Goal Points: " + playerInfo.getComGoalPoints()[0] + "," + playerInfo.getComGoalPoints()[1], AnsiEscapeCodes.GAME_MESSAGE);
            }
        }
    }

    /**
     * This method is called by display to print the board
     */
    private void printBoard() {
        printMessage("Board:", AnsiEscapeCodes.GAME_MESSAGE);
        printBoardOrShelf(AppConstants.BOARD_DIMENSION, AppConstants.BOARD_DIMENSION, gameInfo.getGameBoard());
    }

    /**
     * This method is called by display to print the shelf of the current player
     */
    //TODO: add char to sign personal goal
    private void printMyShelf() {
        for (PlayerInfo playerInfo : gameInfo.getPlayerInfosList()) {
            if (playerInfo.getNickname().equals(this.myNickname)) {
                printMessage("My shelf:", AnsiEscapeCodes.GAME_MESSAGE);
                printBoardOrShelf(AppConstants.ROWS_NUMBER, AppConstants.COLS_NUMBER, playerInfo.getShelf());
                printMessage("   Common Goal Points: " + playerInfo.getComGoalPoints()[0] + "," + playerInfo.getComGoalPoints()[1], AnsiEscapeCodes.GAME_MESSAGE);
                return;
            }
        }
    }

    /**
     * This method is called to print the board or a shelf
     * @param xMax the number of rows
     * @param yMax the number of columns
     */
    private void printBoardOrShelf(int yMax, int xMax, Tile[][] boardOrShelf) {
        StringBuilder lineBuilder;
        for (int i = 0; i < yMax; i++) {
            lineBuilder = new StringBuilder();
            for (int j = 0; j < xMax; j++) {
                lineBuilder.append(tileColorToAnsiCode(boardOrShelf[i][j].getColor())).append("   ").append(AnsiEscapeCodes.ENDING_CODE.getCode());
            }
            System.out.println(lineBuilder);
        }
    }

    /**
     * This method is called by printBoard to convert a tile color to an ANSI escape code
     * @param color the color to convert
     * @return the ANSI escape code
     */
    private String tileColorToAnsiCode(TileColor color) {
        return switch (color) {
            case WHITE -> AnsiEscapeCodes.WHITE_BACKGROUND.getCode();
            case BLUE -> AnsiEscapeCodes.BLUE_BACKGROUND.getCode();
            case YELLOW -> AnsiEscapeCodes.YELLOW_BACKGROUND.getCode();
            case VIOLET -> AnsiEscapeCodes.VIOLET_BACKGROUND.getCode();
            case CYAN -> AnsiEscapeCodes.CYAN_BACKGROUND.getCode();
            case GREEN -> AnsiEscapeCodes.GREEN_BACKGROUND.getCode();
            case EMPTY -> AnsiEscapeCodes.EMPTY_BACKGROUND.getCode();
            default -> AnsiEscapeCodes.DEFAULT_BACKGROUND.getCode();
        };
    }

    /**
     * This method is called by display to print the common goals
     */
    private void printCommonGoals(int goalIndex1, int goalIndex2) {
        printMessage("Common goals:", AnsiEscapeCodes.GAME_MESSAGE);
        printMessage("1) " + getGoalDescription(goalIndex1), AnsiEscapeCodes.GAME_MESSAGE);
        printMessage("   Points for this goal: " + gameInfo.getCommonGoalsStack().get(0), AnsiEscapeCodes.GAME_MESSAGE);
        printMessage("2) " + getGoalDescription(goalIndex2), AnsiEscapeCodes.GAME_MESSAGE);
        printMessage("   Points for this goal: " + gameInfo.getCommonGoalsStack().get(1), AnsiEscapeCodes.GAME_MESSAGE);
    }

    /**
     * This method is called by printCommonGoals to get the description of a goal
     * @param goalIndex the index of the goal
     * @return the description of the goal
     */
    public String getGoalDescription(int goalIndex) {
        return switch (goalIndex) {
            case 0 -> "6 groups of 2 tiles";
            case 1 -> "4 groups of 4 tiles";
            case 2 -> "4 corners of the same color";
            case 3 -> "Two squares of 2x2 tiles";
            case 4 -> "3 columns formed by 6 tiles of maximum 3 different colors";
            case 5 -> "8 tiles of the same color";
            case 6 -> "5 tiles of the same color forming a diagonal";
            case 7 -> "4 rows formed by 6 tiles of maximum 3 different colors";
            case 8 -> "2 columns formed by 6 tiles of 6 different colors";
            case 9 -> "2 rows formed by 6 tiles of 6 different colors";
            case 10 -> "5 tiles of the same color forming a cross";
            default -> "Ladder";
        };
    }

    /**
     * This method is called by start to wait for a command from the player
     *
     * @return the command represented as a string
     */
    @Override
    public String waitCommand() {
        printMessage("Waiting for command (/help for command list) ", AnsiEscapeCodes.INFO_MESSAGE);

        return scanner.nextLine();
    }

    /**
     * This method is called by waitCommand to parse the command and call the right method
     * @param command the command to parse
     */
    @Override
    protected void parseCommand(String command) {
        switch (command.trim()) {
            case "/help" -> {
                printMessage("Command list:", AnsiEscapeCodes.INFO_MESSAGE);
                printMessage("/help: show this list", AnsiEscapeCodes.INFO_MESSAGE);
                printMessage("/move: move a tile", AnsiEscapeCodes.INFO_MESSAGE);
                printMessage("/chat: send a message to the chat", AnsiEscapeCodes.INFO_MESSAGE);
                printMessage("/exit: exit the game", AnsiEscapeCodes.INFO_MESSAGE);
            }
            case "/move" -> parseMoveCommand(command);
            case "/chat" -> chatCommand();
            case "/exit" -> confirmExit();
            default -> printMessage("invalid command, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called by parseCommand to parse a move command
     * @param command the command to parse
     */
    //TODO: change implementation to match changes in parseCommand
    //TODO: add timer to avoid deadlocks
    private void parseMoveCommand(String command) {
        List<Position> positions = new ArrayList<>();
        int column;
        synchronized (displayLock) {
            if (!isMyTurn()) {
                printMessage("Error: please wait for your turn ", AnsiEscapeCodes.ERROR_MESSAGE);
                return;
            }

            printMessage("Select a the tiles you want to pick (x1,y1 x2,y2 x3,y3) ", AnsiEscapeCodes.INFO_MESSAGE);
            String input = scanner.nextLine();
            while (!input.matches("^[0-9]+,[0-9]+$") && !input.matches("^[0-9]+,[0-9]+ [0-9]+,[0-9]+$") && !input.matches("^[0-9]+,[0-9]+ [0-9]+,[0-9]+ [0-9]+,[0-9]+$")) {
                printMessage("Invalid input, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
                input = scanner.nextLine();
            }

            for (String position : input.split(" ")) {
                positions.add(new Position(Integer.parseInt(position.split(",")[0]), Integer.parseInt(position.split(",")[1])));
            }

            printMessage("Select the column were you want to place the tiles ", AnsiEscapeCodes.INFO_MESSAGE);

            input = scanner.nextLine();
            while (!input.matches("^[0-4]+$")) {
                printMessage("Invalid input, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
                input = scanner.nextLine();
            }

            column = Integer.parseInt(input);
        }

            try {
                client.makeMove(positions, column);
                printMessage("Move sent ", AnsiEscapeCodes.INFO_MESSAGE);
            } catch (InvalidNicknameException e) {
                printMessage("Error: invalid nickname ", AnsiEscapeCodes.ERROR_MESSAGE);
            } catch (InvalidMoveException e) {
                printMessage("Error: invalid move please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
            }
    }

    /**
     * This method is called by parseCommand to handle a chat command
     */
    private void chatCommand() {
        synchronized (displayLock) {
            scheduler.schedule(() -> {
                printMessage("To send a global message write 'all : message'", AnsiEscapeCodes.INFO_MESSAGE);
                printMessage("To send a message to a specific player write 'player_name : message' ", AnsiEscapeCodes.INFO_MESSAGE);

                boolean messageSent = false;

                while (!messageSent) {
                    String input = scanner.nextLine();
                    while (!input.matches("^[A-Za-z0-9+_.-]+ : (.+)$")) {
                        printMessage("Invalid message format, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
                        input = scanner.nextLine();
                    }

                    String receiverNickname = input.substring(0, input.indexOf(":")).trim();
                    String message = input.substring(input.indexOf(":") + 1).trim();
                    if (receiverNickname.equals("all")) {
                        client.messageAll(message);
                        messageSent = true;
                    }
                    else {
                        if (checkExistingNickname(receiverNickname)) {
                            client.messageSomeone(message, receiverNickname);
                            messageSent = true;
                        }
                        else {
                            printMessage("This player does not exist, please type again your message: ", AnsiEscapeCodes.ERROR_MESSAGE);
                        }
                    }
                }
            }, 0, TimeUnit.SECONDS);
            try {
                if (scheduler.awaitTermination(20, TimeUnit.SECONDS)) {
                    printMessage("Message sent", AnsiEscapeCodes.INFO_MESSAGE);
                }
                else {
                    printMessage("You took too long to send your message, please try again", AnsiEscapeCodes.ERROR_MESSAGE);
                }
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * This method is called by chatCommand to check if the given nickname exists
     * @param nickname nickname to check
     * @return true if the nickname is present
     */
    private boolean checkExistingNickname(String nickname) {
        return gameInfo.getPlayerInfosList().stream().map(PlayerInfo::getNickname).anyMatch(n->n.equals(nickname));
    }

    /**
     * This method is called by parseCommand to ask the player to confirm she wants to exit
     */
    private void confirmExit() {
        synchronized (displayLock) {
            scheduler.schedule(() -> {
                printMessage("Are you sure you want to exit? (y/n) ", AnsiEscapeCodes.INFO_MESSAGE);
                String input = scanner.nextLine();
                input = input.trim();
                while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                    printMessage("Invalid input, please try again", AnsiEscapeCodes.ERROR_MESSAGE);
                    input = scanner.nextLine();
                    input = input.trim();
                }

                if (input.equalsIgnoreCase("y")) {
                    close("Client closing, bye bye!");
                }
                else {
                    printMessage("Returning to game ", AnsiEscapeCodes.INFO_MESSAGE);
                }
            }, 0, TimeUnit.SECONDS);
            try {
                if (!scheduler.awaitTermination(20, TimeUnit.SECONDS)) {
                    printMessage("You took too long to confirm exit, returning to game...", AnsiEscapeCodes.ERROR_MESSAGE);
                }
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * This method is called by parseCommand to print an error message if the command is invalid
     */
    private void printMessage(String message, AnsiEscapeCodes message_type) {
        System.out.println(message_type.getCode() + message + AnsiEscapeCodes.ENDING_CODE.getCode());
    }

    /**
     * This method is called by start to ask the player if he wants to connect via rmi or socket
     */
    @Override
    // ask for connection type using println, 1 for RMI, 2 for Socket, ask again if the input is not valid
    public void chooseConnectionType() {
        printMessage("Choose connection type (rmi/socket)", AnsiEscapeCodes.INFO_MESSAGE);
        String input = scanner.nextLine();
        input = input.trim();
        while (!input.equalsIgnoreCase("rmi") && !input.equalsIgnoreCase("socket")) {
            printMessage("Invalid input, please try again ", AnsiEscapeCodes.INFO_MESSAGE);
            input = scanner.nextLine();
            input = input.trim();
        }

        if (input.equalsIgnoreCase("rmi")) {
            try {
                client = new RmiClient(myNickname, this);
            } catch (RemoteException | NotBoundException | InterruptedException e) {
                printMessage("error while connecting to the server", AnsiEscapeCodes.ERROR_MESSAGE);
                close("Client closing, try again later");
            }
        }
        else {
            //TODO: implement socket client
            // client = new SocketClient();
        }
    }

    /**
     * This method is called by start to ask the player his nickname and send it to the server
     */
    @Override
    public void askNickname() {
        printMessage("Please insert your nickname: ", AnsiEscapeCodes.INFO_MESSAGE);
        myNickname = scanner.nextLine();
        while (myNickname.equals("") || !client.chooseNickname(myNickname)) {
            printMessage("Invalid nickname, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
            myNickname = scanner.nextLine();
        }
    }

    /**
     * This method is called by start to ask the player if he wants to create a new game or join an existing one
     */
    @Override
    public void createOrJoinGame() {
        boolean gameSelected = false;

        printMessage("Do you want to create a new game or join an existing one? (c/j) ", AnsiEscapeCodes.INFO_MESSAGE);

        while (!gameSelected) {
            String input = scanner.nextLine();
            while (!input.equals("c") && !input.equals("j")) {
                printMessage("Invalid input, please try again ", AnsiEscapeCodes.INFO_MESSAGE);
                input = scanner.nextLine();
            }

            if (input.equals("c")) {
                printMessage("Please insert the number of players ", AnsiEscapeCodes.INFO_MESSAGE);
                String playersNumber = scanner.nextLine();
                while (!playersNumber.matches("[2-4]")) {
                    printMessage("Invalid input, please try again ", AnsiEscapeCodes.INFO_MESSAGE);
                    playersNumber = scanner.nextLine();
                }

                try {
                    client.createGame(Integer.parseInt(playersNumber));
                } catch (NonExistentNicknameException | AlreadyInGameException e) {
                    throw new RuntimeException(e);
                }
                gameSelected = true;
            }
            else {
                try {
                    try {
                        client.joinGame();
                    } catch (NonExistentNicknameException | AlreadyInGameException e) {
                        throw new RuntimeException(e);
                    }
                    gameSelected = true;
                } catch (NoGamesAvailableException e) {
                    printMessage("No games available, please create a new one ", AnsiEscapeCodes.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * This method is called by start to ask the player if he wants to play again
     *
     * @return true if the player wants to play again, false otherwise
     */
    @Override
    public boolean askIfWantToPlayAgain() {
        printMessage("Do you want to play again? (y/n) ", AnsiEscapeCodes.INFO_MESSAGE);
        String input = scanner.nextLine();
        while (!input.equals("y") && !input.equals("n")) {
            printMessage("Invalid input, please try again ", AnsiEscapeCodes.ERROR_MESSAGE);
            input = scanner.nextLine();
        }

        return input.equals("y");
    }

    /**
     * This method is called by close to notify the player that the client is shutting down
     *
     * @param message the message to display
     */
    @Override
    protected void notifyClose(String message) {
        printMessage(message, AnsiEscapeCodes.INFO_MESSAGE);
        try {
            wait(3000);
        } catch (InterruptedException ignored) { }
    }

    /**
     * This method receive a chat message from the server and displays it
     * @param message the message to display
     */
    @Override
    public void displayChatMessage(String message) {
        printMessage(message, AnsiEscapeCodes.CHAT_MESSAGE);
    }
}
