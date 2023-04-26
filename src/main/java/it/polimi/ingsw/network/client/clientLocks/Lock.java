package it.polimi.ingsw.network.client.clientLocks;

import it.polimi.ingsw.network.messages.Message;


// this class works as a Lock and also as a bridge between the outgoing and ingoing TCP messages
public class Lock {

    private Message message;
    private boolean toWait;
    private boolean disconnection;

    public Lock(){
        this.reset();
        this.disconnection = false;
    }

    public Message getMessage(){
        return this.message;
    }

    public void setMessage(Message m){
        this.message = m;
    }

    public void reset(){
        this.message = null;
        this.toWait = true;
    }

    public boolean toWait(){
        return this.toWait;
    }

    public void setToWait(boolean flag){
        this.toWait = flag;
    }

    public boolean isDisconnection() {
        return disconnection;
    }

    public void setDisconnection(boolean flag){
        this.disconnection = flag;
    }
}
