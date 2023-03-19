package it.polimi.ingsw.model.utilities;

import java.util.Random;

/**
 * SINGLETON
 */
public class RandomSingleton {
    /**
     *
     */
    private static Random random;

    /**
     *
     */
    private RandomSingleton(){}

    /**
     *
     */
    public static Random getRandomSingleton(){
        if(random==null) random=new Random();

        return random;
    }
}
