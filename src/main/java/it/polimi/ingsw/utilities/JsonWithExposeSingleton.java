package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton used to get a gson object to parse json files
 */
public class JsonWithExposeSingleton {
    /**
     * Json parser
     */
    private static Gson json;

    /**
     *  Constructor of the singleton (empty)
     */
    private JsonWithExposeSingleton(){}

    /**
     * Method that gets the instance of the parser and returns it (if there is none it creates it)
     * @return the json parser
     */
    public static Gson getJsonWithExposeSingleton(){
        if(json==null) json=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        return json;
    }

}
