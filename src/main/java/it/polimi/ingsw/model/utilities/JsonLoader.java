package it.polimi.ingsw.model.utilities;

import com.google.gson.Gson;

/**
 * SINGLETON
 */
public class JsonLoader {
    /**
     *
     */
    private static Gson json;

    /**
     *
     */
    private JsonLoader(){}

    /**
     *
     */
    public static Gson getJsonLoader(){
        if(json==null) json=new Gson();
        
        return json;
    }
}
