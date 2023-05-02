package it.polimi.ingsw;

import it.polimi.ingsw.network.server.MainServer;
import it.polimi.ingsw.view.ViewLauncher;

import java.rmi.RemoteException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws RemoteException {

        if(args.length>0){

            if(args[0].equals("--server")) MainServer.main(args);
            else if(args[0].equals("--client")) ViewLauncher.main(args);

        }
        else System.out.println("Start with an argument");

    }
}
