package it.polimi.ingsw.launchers;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final Map<String, Runnable> startCommands = new HashMap<>();

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
        else System.out.println("You need to start with an argument. Use --help for more information.");

    }
}
