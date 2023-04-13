package it.polimi.ingsw.view;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.model.Position;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent the CLI view of the game
 */
public class CLI extends View{

    /**
     * This method is called by update to display the game
     */
    @Override
    protected void display() {

    }

    /**
     * This method is called by start to wait for a command from the player
     *
     * @return the command represented as a string
     */
    @Override
    public String waitCommand() {
        System.out.println("Waiting for command (/help for command list)");
        String command = System.console().readLine();

        return command;
    }

    /**
     * This method is called by waitCommand to parse the command and call the right method
     * @param command the command to parse
     */
    @Override
    protected void parseCommand(String command) {
        // if it's not the player's turn, the command will not be sent to the server
        if (!isMyTurn()) {
            System.out.println("Error: please wait for your turn");
            return;
        }

        switch (command.substring(0, 2)) {
            case "/h":
                System.out.println("Command list:");
                System.out.println("/m: move a tile");
                System.out.println(" - syntax: /m x1,y1 [x2,y2 x3,y3] columnNumber");
                System.out.println(" - [] indicates a facultative parameter");
                System.out.println("/c: send a chat message");
                System.out.println(" - syntax: /c [playerName]: message");
                System.out.println(" - [] indicates a facultative parameter, if no player name is specified the message is global");
                System.out.println("/e: exit the game");
                break;
            case "/m":
                parseMoveCommand(command);
                break;
            case "/c":
                parseChatCommand(command);
                break;
            case "/e":
                confirmExit();
                break;
            default:
                errorMessage("invalid command, please try again");
        }
    }

    /**
     * This method is called by parseCommand to parse a move command
     * @param command the command to parse
     */
    private void parseMoveCommand(String command) {
        int tilesNumber = (command.length() - 3) / 4;

        if (tilesNumber < 1 || tilesNumber > 3) {
            errorMessage("invalid move, please try again");
            return;
        }

        if (!gameInfo.getCurrentPlayerNickname().equals(myNickname)) {
            errorMessage("is not your turn, please wait");
            return;
        }

        int i;
        List<Position> tiles = new ArrayList<>();
        for (i = 0; i < tilesNumber; i++) {
            tiles.add(new Position(Integer.parseInt(command.substring(3 + i * 4, 4 + i * 4)), Integer.parseInt(command.substring(5 + i * 6, 8 + i * 4))));
        }

        int column = Integer.parseInt(command.substring(3 + i * 4, 4 + i * 4));

        if (checkValidMove(tiles, column)) {
            try {
                client.makeMove(tiles, column);
            } catch (InvalidNicknameException e) {
                errorMessage("is not your turn, please wait");
            } catch (InvalidMoveException e) {
                errorMessage("invalid move, please try again");
            }
        }
        else {
            errorMessage("invalid move, please try again");
        }
    }

    /**
     * This method is called by parseCommand to parse a chat command
     * @param command the command to parse
     */
    private void parseChatCommand(String command) {
        if (command.charAt(3) == ':') {
            String message = command.substring(4);
            client.messageAll(message);
        }
        else {
            String receiver = command.substring(3);
            receiver = receiver.substring(0, receiver.indexOf(':'));

            String message = command.substring(command.indexOf(':') + 2);

            client.messageSomeone(receiver, message);
        }
    }

    /**
     * This method is called by parseCommand to ask the player to confirm she wants to exit
     */
    private void confirmExit() {
        System.out.println("Are you sure you want to exit? (y/n)");
        String input = System.console().readLine();
        while (!input.equals("y") && !input.equals("n")) {
            System.out.println("Invalid input, please try again");
            input = System.console().readLine();
        }

        if (input.equals("y")) {
            close("Client closing, bye bye!");
        }
    }

    /**
     * This method is called by parseCommand to print an error message if the command is invalid
     */
    private void errorMessage(String message) {
        System.out.println(message);
    }

    /**
     * This method is called by start to ask the player if he wants to connect via rmi or socket
     */
    @Override
    // ask for connection type using println, 1 for RMI, 2 for Socket, ask again if the input is not valid
    public void chooseConnectionType() {
        System.out.println("Choose connection type: 1 for RMI, 2 for Socket");
        String input = System.console().readLine();
        while (!input.equals("1") && !input.equals("2")) {
            System.out.println("Invalid input, please try again");
            input = System.console().readLine();
        }

        if (input.equals("1")) {
            try {
                client = new RmiClient(myNickname, this);
            } catch (RemoteException | NotBoundException | InterruptedException e) {
                errorMessage("error while connecting to the server");
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
        System.out.println("Please insert your nickname");
        myNickname = System.console().readLine();
        while ((myNickname == null || myNickname.equals("")) && !client.chooseNickname(myNickname)) {
            System.out.println("Invalid nickname, please try again");
            myNickname = System.console().readLine();
        }
    }

    /**
     * This method is called by start to ask the player if he wants to create a new game or join an existing one
     */
    @Override
    public void createOrJoinGame() {

    }

    /**
     * This method is called by start to ask the player if he wants to play again
     *
     * @return true if the player wants to play again, false otherwise
     */
    @Override
    public boolean askIfWantToPlayAgain() {
        System.out.println("Do you want to play again? (y/n)");
        String input = System.console().readLine();
        while (!input.equals("y") && !input.equals("n")) {
            System.out.println("Invalid input, please try again");
            input = System.console().readLine();
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
        System.out.println(message);
        try {
            wait(3000);
        } catch (InterruptedException ignored) { }
    }

    @Override
    public void displayChatMessage(String message) {
        System.out.println(message);
    }
}
