package it.polimi.ingsw.network.client.exceptions;

/**
 * Exception that arises if the server is too slow to respond to the client
 */
public class TimeOutException extends Exception{
    public TimeOutException(){
        super();
    }
}
