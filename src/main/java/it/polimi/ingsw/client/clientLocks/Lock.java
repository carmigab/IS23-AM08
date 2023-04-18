package it.polimi.ingsw.client.clientLocks;

import it.polimi.ingsw.server.messages.Message;

public class Lock {
    private boolean waiting = false;

    private Message message;

    public boolean hasBeenWokeUp(){
        return waiting;
    }

    public void wakeUp(boolean b) {
        this.waiting = b;
    }

    public Message getMessage(){
        return this.message;
    }

    public void setMessage(Message m){
        this.message = m;
    }

}
