package it.polimi.ingsw.dummies;



import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class ViewLauncher3 {
    private static View view = null;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Select if you want to play with text or graphic interface (type cli/gui):");
        String input = scanner.nextLine();
        input = input.trim();
        while (!input.equalsIgnoreCase("cli") && !input.equalsIgnoreCase("gui")) {
            System.out.println("Invalid command, please try again");
            input = scanner.nextLine();
            input = input.trim();
        }

        switch (input.toLowerCase()) {
            case "cli" -> view = new CLI();
            // case "gui" -> view = new GUI();
            default -> view = new CLI();
        }

        new Thread(view::getUserInput).start();
        new Thread(view::checkForShutdown).start();
    }
}
