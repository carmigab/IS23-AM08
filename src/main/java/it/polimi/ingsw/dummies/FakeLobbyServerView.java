package it.polimi.ingsw.dummies;

import it.polimi.ingsw.server.RMILobbyServerInterface;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FakeLobbyServerView {

    public static void main(String[] args) {
        try {
            Integer remoteObjectPort=12345;
            System.out.println("Locating registry...");
            Registry registry= LocateRegistry.getRegistry(remoteObjectPort);
            System.out.println("Registry found...");
            System.out.println("All RMI Registry bindings: ");
            List<String>bindingsList= Arrays.stream(registry.list()).toList();
            for(String binding: bindingsList){
                System.out.println(binding);
            }
            String remoteObjectName="LobbyServer";
            System.out.println("Locating remote object "+remoteObjectName+" ...");
            RMILobbyServerInterface remoteServer= (RMILobbyServerInterface) registry.lookup(remoteObjectName);
            System.out.println("Server located, you can start to send messages to it");

            Scanner scanner=new Scanner(System.in);
            boolean end =false;
            while(!end){
                String command=scanner.nextLine();
                switch (command){
                    case "name":
                        System.out.println("Inserisci nome");
                        String nome=scanner.nextLine();
                        System.out.println(remoteServer.chooseNickname(nome));
                        break;
                    case "adios":
                        end=true;
                        break;

                }
            }
        }
        catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NotBoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
