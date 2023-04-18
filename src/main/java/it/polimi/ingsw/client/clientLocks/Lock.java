package it.polimi.ingsw.client.clientLocks;

import it.polimi.ingsw.server.messages.Message;


// this class works as a Lock and also as a bridge between the outgoing and ingoing TCP messages
public class Lock {

    private Message message;

    public Lock(){
        this.reset();
    }

    public Message getMessage(){
        return this.message;
    }

    public void setMessage(Message m){
        this.message = m;
    }

    public void reset(){
        this.message = null;
    }

}
