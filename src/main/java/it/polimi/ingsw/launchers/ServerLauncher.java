package it.polimi.ingsw.launchers;

import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.network.server.LobbyServerConfig;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.utilities.UtilityFunctions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main server of the application
 */
public class ServerLauncher {


    private static final Map<String, Consumer<Integer>> serverParameters=new HashMap<>();

    public static void main(String[] args) {

        List<String> argsToList=new ArrayList<>(List.of(args));

        if(!argsToList.isEmpty())argsToList.remove(0);
        LobbyServerConfig input= JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(ServerConstants.SERVER_INITIAL_CONFIG_FILENAME, ServerLauncher.class),LobbyServerConfig.class);

        serverParameters.put("--tcp-port"   , (index) -> input.setServerPortTCP(Integer.valueOf(argsToList.get(index+1))));
        serverParameters.put("--rmi-port   ", (index) -> input.setServerPortRMI(Integer.valueOf(argsToList.get(index+1))));
        serverParameters.put("--server-name", (index) -> input.setServerName(argsToList.get(index+1)));
        serverParameters.put("--game-name"  , (index) -> input.setStartingName(argsToList.get(index+1)));


        if(argsToList.size() > 0 && argsToList.get(0).equals("--help")){
            System.out.println("""
                    Usage:\s
                    --tcp-port
                    --rmi-port
                    --server-name
                    --game-name""");
            return;
        }
        for(int i=0; i<argsToList.size();i+=2){
            if(serverParameters.containsKey(argsToList.get(i))) serverParameters.get(argsToList.get(i)).accept(i);
        }

        try {
            LobbyServer lobbyS = new LobbyServer(input);
            lobbyS.start();
        }
        catch (RemoteException e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }



    }
}
