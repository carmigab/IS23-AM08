package it.polimi.ingsw.launchers;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main application launched from the jar
 *
 */
public class App 
{

    /**
     * This map contains the actions to perform based on the parameters passed as arguments to the application
     */
    private static final Map<String, Runnable> startCommands = new HashMap<>();

    /**
     * The main of the application, it decides which mode to launch (server or client)
     * Without parameters the application will launch in client mode
     * @param args arguments passed via command line
     */
    public static void main( String[] args ) {

        startCommands.put("--server", () -> ServerLauncher.main(args));
        startCommands.put("--client", () -> ViewLauncher.main(args));
        startCommands.put("--help", () -> System.out.println("""
                Usage:\s
                --server to launch the application as a server\s
                --client to launch the application as a client"""));

        if(args.length>0){

            if(startCommands.containsKey(args[0])) startCommands.get(args[0]).run();

        }
        else ViewLauncher.main(args);

    }
}
