package it.polimi.ingsw.network.client.clientLocks;

import it.polimi.ingsw.network.messages.Message;


/**
 * this class works as a Lock and also as a bridge between the outgoing and ingoing TCP messages
 */
public class Lock {
    /**
     * The message
     */
    private Message message;
    /**
     * Flag that is true if the object synchronized on the lock has to wait
     */
    private boolean toWait;
    /**
     * Flag that is true if the client has disconnected
     */
    private boolean offline;

    /**
     * constructor of the class
     */
    public Lock(){
        this.reset();
        this.offline = false;
    }

    /**
     * Method to get the message memorized on the lock
     * @return the message
     */
    public Message getMessage(){
        return this.message;
    }

    /**
     * Method to set a message on the lock
     * @param m : the message
     */
    public void setMessage(Message m){
        this.message = m;
    }

    /**
     * Method to reset the lock to initial conditions
     */
    public void reset(){
        this.message = null;
        this.toWait = true;
    }

    /**
     * Method that returns the toWait flag
     * @return the toWait flag
     */
    public boolean toWait(){
        return this.toWait;
    }

    /**
     * Method that sets the toWait flag
     * @param flag: new value
     */
    public void setToWait(boolean flag){
        this.toWait = flag;
    }

    /**
     * Method that returns the disconnection flag
     * @return the disconnection flag
     */
    public boolean isOffline() {
        return offline;
    }

    /**
     * Method that sets the disconnection flag
     * @param flag: the new flag
     */
    public void setOffline(boolean flag){
        this.offline = flag;
    }
}
