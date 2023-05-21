package it.polimi.ingsw.view;

public class GUIView extends View{


    @Override
    protected void display() {
        this.controller.display();
    }

    @Override
    protected void welcome() {

    }

    @Override
    protected void waitForGameStart() {

    }

    @Override
    protected String waitCommand() {
        return null;
    }

    @Override
    protected void parseCommand(String command) {

    }

    @Override
    protected void chooseConnectionType() {

    }

    @Override
    protected void askNickname() {

    }

    @Override
    protected void createOrJoinGame() {

    }

    @Override
    protected boolean askIfWantToPlayAgain() {
        return false;
    }

    @Override
    protected void notifyClose(String message) {

    }

    @Override
    public void displayChatMessage(String message) {

        this.controller.displayMessage(message);

    }
}
