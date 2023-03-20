package it.polimi.ingsw.model.utilities;

import com.google.gson.Gson;

/**
 * SINGLETON
 */
public class JsonSingleton {
    /**
     *
     */
    private static Gson json;

    /**
     *
     */
    private JsonSingleton(){}

    /**
     *
     */
    public static Gson getJsonSingleton(){
        if(json==null) json=new Gson();

        return json;
    }
}
