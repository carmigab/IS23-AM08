package it.polimi.ingsw.utilities;

import java.util.Random;

/**
 * Singleton used to get a random number generator
 */
public class RandomSingleton {
    /**
     * Random variable
     */
    private static Random random;

    /**
     * Constructor of the singleton (empty)
     */
    private RandomSingleton(){}

    /**
     * Method that gets the instance of the Random variable and returns it (if there is none it creates it)
     * @return the Random variable
     */
    public static Random getRandomSingleton(){
        if(random==null) random=new Random();

        return random;
    }
}
