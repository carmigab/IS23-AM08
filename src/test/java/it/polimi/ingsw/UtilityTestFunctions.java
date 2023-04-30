package it.polimi.ingsw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class UtilityTestFunctions {

    public static BufferedReader getReaderFromFileNameRelativePath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(c.getResourceAsStream(fileName))));
    }

    public static BufferedReader getReaderFromFileNameResourcesPath(String fileName, Class c){
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(c.getClassLoader().getResourceAsStream(fileName))));
    }

}
