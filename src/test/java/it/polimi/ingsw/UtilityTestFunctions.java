package it.polimi.ingsw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * This class represents a collection of functions used for all tests
 */
public class UtilityTestFunctions {

    /**
     * This method returns a file reader from the filename requested in input searching from its relative path
     * @param fileName name of the file to be loaded
     * @param c class calling the method
     * @return the BufferedReader of the file
     */
    public static BufferedReader getReaderFromFileNameRelativePath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(c.getResourceAsStream(fileName))));
    }

    /**
     * This method returns a file reader from the filename requested in input searching from the absolute path of the project
     * @param fileName name of the file to be loaded
     * @param c class calling the method
     * @return the BufferedReader of the file
     */
    public static BufferedReader getReaderFromFileNameResourcesPath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(c.getClassLoader().getResourceAsStream(fileName))));
    }

}
