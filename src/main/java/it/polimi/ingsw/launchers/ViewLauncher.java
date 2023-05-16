package it.polimi.ingsw.launchers;

import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class ViewLauncher {
    private static View view = null;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        //System.out.println("ඞ");

        System.out.println("Select if you want to play with text or graphic interface (type cli/gui):");
        String input = scanner.nextLine();
        input = input.trim();
        while (!input.equalsIgnoreCase("cli") && !input.equalsIgnoreCase("gui")) {
            System.out.println("Invalid command, please try again");
            input = scanner.nextLine();
            input = input.trim();
        }

        switch (input.toLowerCase()) {
            case "cli" -> {
                view = new CLI();
                new Thread(view::getUserInput).start();
//                new Thread(view::checkForShutdown).start();
            }
             case "gui" -> GUILauncher.main(new String[1]);
            default -> {
                view = new CLI();
                new Thread(view::getUserInput).start();
//                new Thread(view::checkForShutdown).start();
            }
        }
    }
}
