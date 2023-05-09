package it.polimi.ingsw.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * This class is a utility for loading files from the resources directory
 */
public class UtilityFunctions {
    /**
     * Method that returns the reader gotten from the file read from the relative path of the calling class in the resources directory
     * @param fileName file name to be loaded
     * @param c class that calls the method
     * @return buffered reader of the file to be read
     */
    public static BufferedReader getReaderFromFileNameRelativePath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(getInputStreamFromFileNameRelativePath(fileName, c)));
    }

    /**
     * Method that returns the input reader gotten from the file read from the relative path of the calling class in the resources directory
     * @param fileName file name to be loaded
     * @param c class that calls the method
     * @return input stream of the file to be read
     */
    public static InputStream getInputStreamFromFileNameRelativePath(String fileName, Class c){
        return Objects.requireNonNull(c.getResourceAsStream(fileName));
    }
    /**
     * Method that returns the reader gotten from the file read from the relative path of the resources directory
     * @param fileName file name to be loaded
     * @param c class that calls the method
     * @return buffered reader of the file to be read
     */
    public static BufferedReader getReaderFromFileNameResourcesPath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(c.getClassLoader().getResourceAsStream(fileName))));
    }

}
