package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.utilities.UtilityFunctions;
import it.polimi.ingsw.view.gui.ClickableComponent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * This class is the whole controller of the game scene
 */
public class GameViewController implements Initializable{
    /**
     * This attribute stores the grid panel that decides how to view each component
     */
    @FXML
    private GridPane gridPane;
    /**
     * This attribute stores the image information of the game board
     */
    @FXML
    private ImageView gameBoardImage;
    /**
     * This attribute stores the panel used to contain all the components of the the game board (image, all the tiles, canvas)
     */
    @FXML
    private AnchorPane gameBoardAnchorPane;
    /**
     * This attribute stores the canvas put on top of the game board used for the recognition of the mouse interactions
     */
    @FXML
    private Canvas gameBoardCanvas;
    /**
     * This attribute stores the image information of my shelf
     */
    @FXML
    private ImageView myShelfImage;
    /**
     * This attribute stores the panel used to contain all the components of my shelf
     */
    @FXML
    private AnchorPane myShelfAnchorPane;
    /**
     * This attribute stores the canvas relative to my shelf, where the click actions will be performed
     */
    @FXML
    private Canvas myShelfCanvas;
    /**
     * This attribute stores the image information of the first common goal
     */
    @FXML
    private ImageView commonGoal1Image;
    /**
     * This attribute stores the panel used to contain all the components of the first common goal
     */
    @FXML
    private AnchorPane commonGoal1AnchorPane;
    /**
     * This attribute stores the canvas relative to the first common goal, where the click actions will be performed
     */
    @FXML
    private Canvas commonGoal1Canvas;
    /**
     * This attribute stores the image information of the second common goal
     */
    @FXML
    private ImageView commonGoal2Image;
    /**
     * This attribute stores the panel used to contain all the components of the second common goal
     */
    @FXML
    private AnchorPane commonGoal2AnchorPane;
    /**
     * This attribute stores the canvas relative to the second common goal, where the click actions will be performed
     */
    @FXML
    private Canvas commonGoal2Canvas;
    /**
     * This attribute stores the image information of the personal goal
     */
    @FXML
    private ImageView personalGoalImage;
    /**
     * This attribute stores the panel used to contain all the components of the personal goal
     */
    @FXML
    private AnchorPane personalGoalAnchorPane;
    /**
     * This attribute stores the canvas relative to the personal goal, where the click actions will be performed
     */
    @FXML
    private Canvas personalGoalCanvas;
    /**
     * This attribute stores the image information of the move list
     */
    @FXML
    private ImageView moveListImage;
    /**
     * This attribute stores the panel used to contain all the components of the move list
     */
    @FXML
    private AnchorPane moveListAnchorPane;
    /**
     * This attribute stores the canvas relative to the move list, where the click actions will be performed
     */
    @FXML
    private Canvas moveListCanvas;
    /**
     * This attribute stores the image information of the points obtained by the player
     */
    @FXML
    private ImageView myPointsImage;
    /**
     * This attribute stores the panel used to contain all the components of the the points obtained by the player
     */
    @FXML
    private AnchorPane myPointsAnchorPane;
    /**
     * This attribute stores the canvas relative to the the points obtained by the player, where the click actions will be performed
     */
    @FXML
    private Canvas myPointsCanvas;
    /**
     * This attribute stores the label where every error/success will be printed
     */
    @FXML
    private Label errorLabel;
    /**
     * This attribute stores the scroll pane of the chat section
     */
    @FXML
    private ScrollPane chatScrollPane;
    /**
     * This attribute stores the label where all the chat is shown
     */
    @FXML
    private Label chatLabel;
    /**
     * This attribute stores the text field where the user can input the chat
     */
    @FXML
    private TextField chatTextField;
    /**
     * This attribute stores the button used to send messages to the chat
     */
    @FXML
    private Button chatButton;
    /**
     * This attribute stores the button used to refresh the page
     */
    @FXML
    private Button refreshButton;
    /**
     * This attribute stores the container where all the scene is set
     */
    @FXML
    private BorderPane gameContainer;

    /**
     * This attribute stores the image information of the title
     */
    @FXML
    private ImageView title;

    /**
     * This attribute is a personalized data format used for transporting the position selected on the game board in the clipboard when a drag occurs
     */
    private final DataFormat dft=new DataFormat("gameBoardPosition");

    /*
    @FXML
    private Slider tcoxl;
    @FXML
    private Slider tcoxr;
    @FXML
    private Slider tcoyu;
    @FXML
    private Slider tcoyd;
    @FXML
    private Slider tcodx;
    @FXML
    private Slider tcody;
    */

    /**
     * This attribute is a component that collects all the information of the game board
     */
    private ClickableComponent gameBoard;
    /**
     * This attribute is a component that collects all the information of my shelf
     */
    private ClickableComponent myShelf;
    /**
     * This attribute is a component that collects all the information of the first common goal
     */
    private ClickableComponent commonGoal1;
    /**
     * This attribute is a component that collects all the information of the second common goal
     */
    private ClickableComponent commonGoal2;
    /**
     * This attribute is a component that collects all the information of the personal goal
     */
    private ClickableComponent personalGoal;
    /**
     * This attribute is a component that collects all the information of the move list
     */
    private ClickableComponent moveList;
    /**
     * This attribute is a list of components that collects all the information of the other player's shelves
     */
    private List<ClickableComponent> otherShelf;
    /**
     * This attribute is a list of components that collects all the points obtained during the game of the player
     * (i.e. the common goal points and the first place point)
     */
    private ClickableComponent myPoints;
    /**
     * This attribute is a list of components that collects all the points obtained during the game of the other players
     * (i.e. the common goal points and the first place point)
     */
    private List<ClickableComponent> otherPointsObtained;
    /**
     * This attribute is a map that stores the move selected
     */
    private Map<Integer, Position> positionsList;
    /**
     * This attribute is the reference to the gui component used to call methods via network
     */
    private View guiView;

    /**
     * Setter of the gui view
     * @param guiView gui view to set
     */
    public void setGuiView(View guiView){
        this.guiView=guiView;
    }


    /**
     * Method that is called when the fxml file is loaded
     * It setups all the game board, shelf, common goals, chat for a correct showing of the images
     * It also adds a listener when the component has a change in size
     * It also sets up the grid pane
     * @param url something
     * @param resourceBundle something else
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.positionsList=new HashMap<>();
        this.otherShelf   =new ArrayList<>(ModelConstants.MAX_PLAYERS-1);
        this.otherPointsObtained = new ArrayList<>(ModelConstants.MAX_PLAYERS-1);


        this.initializeClickableComponents();

        this.drawClickableComponents();

        //Prepare the listener of the resize event
        ChangeListener<Number> onDimensionsChange = (observable, oldValue, newValue) ->
                {
                    this.gameBoard   .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.myShelf     .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.commonGoal1 .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.commonGoal2 .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.personalGoal.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.moveList    .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.chatScrollPane.setPrefViewportWidth (Math.min(gameContainer.getWidth(), gameContainer.getHeight())*0.2);
                    this.chatScrollPane.setPrefViewportHeight(Math.min(gameContainer.getWidth(), gameContainer.getHeight())*0.2);
                    for(ClickableComponent clickableComponent: this.otherShelf) clickableComponent.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.myPoints    .setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    for(ClickableComponent clickableComponent: this.otherPointsObtained) clickableComponent.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));

                    this.drawClickableComponents();
                };

        //Add the listeners
        this.gameContainer.widthProperty().addListener(onDimensionsChange);
        this.gameContainer.heightProperty().addListener(onDimensionsChange);

        //this.initializeGridPane();

        this.initializeScene();

        this.chatLabel.setText("");

        /*
        tcoxl.valueProperty().addListener((
                     observableValue,
                    oldValue,
                    newValue) ->{
            System.out.println("tcoxl: "+newValue.doubleValue());
            moveList.setTileComponentOffsetXLeft(newValue.doubleValue());
            moveList.draw();
                });
        tcoxr.valueProperty().addListener((
                observableValue,
                oldValue,
                newValue) ->{
            System.out.println("tcoxr: "+newValue.doubleValue());
            moveList.setTileComponentOffsetXRight(newValue.doubleValue());
            moveList.draw();
        });
        tcoyu.valueProperty().addListener((
                observableValue,
                oldValue,
                newValue) ->{
            System.out.println("tcoyu: "+newValue.doubleValue());
            moveList.setTileComponentOffsetYUp(newValue.doubleValue());
            moveList.draw();
        });
        tcoyd.valueProperty().addListener((
                observableValue,
                oldValue,
                newValue) ->{
            System.out.println("tcoyd: "+newValue.doubleValue());
            moveList.setTileComponentOffsetYDown(newValue.doubleValue());
            moveList.draw();
        });
        tcodx.valueProperty().addListener((
                observableValue,
                oldValue,
                newValue) ->{
            System.out.println("tcodx: "+newValue.doubleValue());
            moveList.setTileComponentDistanceX(newValue.doubleValue());
            moveList.draw();
        });
        tcody.valueProperty().addListener((
                observableValue,
                oldValue,
                newValue) ->{
            System.out.println("tcody: "+newValue.doubleValue());
            moveList.setTileComponentDistanceY(newValue.doubleValue());
            moveList.draw();
        });
         */


    }

    /**
     * This method takes all clickable components and sets them up with their relative values (in percentage)
     */
    private void initializeClickableComponents(){
        //Create the array of tiles
        this.gameBoard=new ClickableComponent(this.gameBoardImage, this.gameBoardAnchorPane, this.gameBoardCanvas, ModelConstants.BOARD_DIMENSION, ModelConstants.BOARD_DIMENSION,
                0.045, 0.045, 0.045, 0.045, 0.0, 0.0, 0.5);
        //Create the array of tiles
        this.myShelf=new ClickableComponent(this.myShelfImage, this.myShelfAnchorPane, this.myShelfCanvas, ModelConstants.ROWS_NUMBER, ModelConstants.COLS_NUMBER,
                0.097, 0.097, 0.054, 0.11, 0.027, 0.019, 0.4);

        this.commonGoal1=new ClickableComponent(this.commonGoal1Image, this.commonGoal1AnchorPane, this.commonGoal1Canvas, 1, 1,
                0.42, 0.0, 0.0, 0.36, 0.13, 0.17, 0.2);
        this.commonGoal2=new ClickableComponent(this.commonGoal2Image, this.commonGoal2AnchorPane, this.commonGoal2Canvas, 1, 1,
                0.42, 0.0, 0.0, 0.36, 0.13, 0.17, 0.2);
        this.personalGoal=new ClickableComponent(this.personalGoalImage, this.personalGoalAnchorPane, this.personalGoalCanvas, 1, 1,
                0.097, 0.097, 0.054, 0.11, 0.027, 0.019, 0.3);
        this.moveList=new ClickableComponent(this.moveListImage, this.moveListAnchorPane, this.moveListCanvas, 1, ModelConstants.MAX_NUM_OF_MOVES,
                0.052, 0.115, 0.068, 0.627, 0.015, 0.013, 0.3);

        for(int i=0;i<ModelConstants.MAX_PLAYERS-1;i++){
            ImageView imageView=new ImageView();
            imageView.setImage(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("gui/images/boards/bookshelf_orth.png", this.getClass())));
            this.otherShelf.add(new ClickableComponent(imageView, new AnchorPane(), new Canvas(), ModelConstants.ROWS_NUMBER, ModelConstants.COLS_NUMBER,
                    0.097, 0.097, 0.054, 0.11, 0.027, 0.019, 0.25));
        }

        this.myPoints=new ClickableComponent(this.myPointsImage, this.myPointsAnchorPane, this.myPointsCanvas, 1, ModelConstants.TOTAL_CG_PER_GAME+1,
                0.052, 0.115, 0.068, 0.627, 0.015, 0.013, 0.3);

        for(int i=0;i<ModelConstants.MAX_PLAYERS-1;i++){
            ImageView imageView= new ImageView();
            imageView.setImage(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("gui/images/screenshot.png", this.getClass())));
            this.otherPointsObtained.add(new ClickableComponent(imageView, new AnchorPane(), new Canvas(), 1, ModelConstants.TOTAL_CG_PER_GAME+1,
                    0.052, 0.115, 0.068, 0.627, 0.015, 0.013, 0.3));
        }

    }

    /**
     * This method draws every clickable component
     */
    private void drawClickableComponents(){
        this.gameBoard.draw();
        this.myShelf.draw();
        this.commonGoal1.draw();
        this.commonGoal2.draw();
        this.personalGoal.draw();
        this.moveList.draw();
        for(ClickableComponent clickableComponent: this.otherShelf) clickableComponent.draw();
        this.myPoints.draw();
        for(ClickableComponent clickableComponent: this.otherPointsObtained) clickableComponent.draw();
    }


    private void initializeScene() {
        title.setFitHeight(100);
        title.setPreserveRatio(true);
        title.setSmooth(true);
        title.setCache(true);
        this.gameContainer.setTop(new HBox(title));
        this.gameContainer.setRight(new VBox(this.myShelfAnchorPane, this.personalGoalAnchorPane, this.myPointsAnchorPane));
//        this.moveListAnchorPane.getTransforms().add(new Rotate(90, 0, 0));
        this.gameContainer.setCenter(new HBox(new VBox(this.gameBoardAnchorPane, new HBox(this.commonGoal1AnchorPane, this.commonGoal2AnchorPane)),
                new VBox(new HBox(this.moveListAnchorPane), new HBox(this.errorLabel), new HBox(this.chatScrollPane))));

        VBox otherShelfVBox = new VBox();
        for(int i=0;i<ModelConstants.MAX_PLAYERS-1;i++)
            otherShelfVBox.getChildren().add(new VBox(new Text("player's " + (i + 1) + " name") , this.otherShelf.get(i).getComponentAnchorPane(), this.otherPointsObtained.get(i).getComponentAnchorPane()));

        this.gameContainer.setLeft(otherShelfVBox);
    }

    /**
     * This method takes the current information from the game and draws it in the container
     */
    public void display(){

        if (this.guiView == null) return;

        if(this.guiView.gameInfo == null) return;

        if(this.guiView.currentState.equals(State.GRACEFULDISCONNECTION)){
            Platform.runLater(()->this.showErrorAlert("Someone disconnected"));
            return;
        }

        if(this.guiView.currentState.equals(State.ENDGAME)) {
            Platform.runLater(this::showGameEndedAlert);
            return;
        }


        if(this.guiView.isMyTurn()) Platform.runLater(()->this.errorLabel.setText("YOUR TURN"));
        else Platform.runLater(()->this.errorLabel.setText("DO NOT MOVE"));



        Platform.runLater(this::displayGameBoard);

        Platform.runLater(this::displayMyShelf);

        Platform.runLater(this::displayCommonGoals);

        Platform.runLater(this::dispayPersonalGoal);

        Platform.runLater(this::clearPositionList);

        Platform.runLater(this::displayOtherShelf);

        Platform.runLater(this::displayAllPointsObtained);
    }

    /**
     * This method takes the information contained in the game info and draws it to the screen
     */
    private void displayGameBoard(){

        for(int i=0; i<ModelConstants.BOARD_DIMENSION;i++){
            for(int j=0;j<ModelConstants.BOARD_DIMENSION;j++){
                Optional<Image> image=this.getImageFromTileDescription(this.guiView.gameInfo.getGameBoard()[i][j]);
                int x=i;
                int y=j;
                image.ifPresentOrElse((smth)->this.gameBoard.setComponentSavedImageFromPositions(image.get(), x, y),
                        ()->this.gameBoard.setComponentSavedImageFromPositions(null, x, y));
            }
        }

    }

    /**
     * This method takes the information contained in the game info and draws it to the screen
     */
    private void displayMyShelf(){

        for(PlayerInfo playerInfo: this.guiView.gameInfo.getPlayerInfosList()){
            if(playerInfo.getNickname().equals(this.guiView.myNickname)){
                for(int i=0; i<ModelConstants.ROWS_NUMBER;i++){
                    for(int j=0; j<ModelConstants.COLS_NUMBER;j++){
                        Optional<Image> image=this.getImageFromTileDescription(playerInfo.getShelf()[i][j]);
                        int x=i;
                        int y=j;
                        image.ifPresentOrElse((smth)->this.myShelf.setComponentSavedImageFromPositions(image.get(), x, y),
                                ()->this.myShelf.setComponentSavedImageFromPositions(null, x, y));
                    }
                }
            }
        }
    }

    /**
     * This method displays the correct image for the personal goal
     */
    private void dispayPersonalGoal(){
        for(PlayerInfo player: this.guiView.gameInfo.getPlayerInfosList()){
            if(player.getNickname().equals(this.guiView.myNickname)){
                this.personalGoal.setComponentImage(this.getImageFromPersonalGoalDescription(player.getPersonalGoalNumber()));
            }
        }
    }

    /**
     * This method displays the correct image and stack values of each common goal
     */
    private void displayCommonGoals(){

        this.commonGoal1.setComponentImage(this.getImageFromCommonGoalDescription(this.guiView.gameInfo.getCommonGoalsCreated().get(0)));
        this.commonGoal2.setComponentImage(this.getImageFromCommonGoalDescription(this.guiView.gameInfo.getCommonGoalsCreated().get(1)));

        if(this.guiView.gameInfo.getCommonGoalsStack().get(0)!=0)
            this.commonGoal1.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(this.guiView.gameInfo.getCommonGoalsStack().get(0)),0,0);
        else this.commonGoal1.setComponentSavedImageFromPositions(null, 0, 0);
        if(this.guiView.gameInfo.getCommonGoalsStack().get(1)!=0)
            this.commonGoal2.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(this.guiView.gameInfo.getCommonGoalsStack().get(1)),0,0);
        else this.commonGoal2.setComponentSavedImageFromPositions(null, 0, 0);
    }

    /**
     * This method resets the move, so that it is clear for the next turn
     */
    private void clearPositionList(){

        this.positionsList.clear();
        for(int i=0;i<ModelConstants.MAX_NUM_OF_MOVES;i++){
            this.moveList.setComponentSavedImageFromPositions(null, 0, i);
        }

    }

    /**
     * This method displays all other shelves
     */
    private void displayOtherShelf(){
        VBox otherShelfBox=new VBox();

        for(int i=0, l=0; i<this.guiView.gameInfo.getPlayerInfosList().size();i++,l++){
            PlayerInfo playerInfo=this.guiView.gameInfo.getPlayerInfosList().get(i);
            if(!playerInfo.getNickname().equals(this.guiView.myNickname)){
                for(int j=0; j<ModelConstants.ROWS_NUMBER;j++){
                    for(int k=0; k<ModelConstants.COLS_NUMBER;k++){
                        Optional<Image> image=this.getImageFromTileDescription(playerInfo.getShelf()[j][k]);
                        int x=j;
                        int y=k;
                        int z=l;
                        image.ifPresentOrElse((smth)->this.otherShelf.get(z).setComponentSavedImageFromPositions(image.get(), x, y),
                                ()->this.otherShelf.get(z).setComponentSavedImageFromPositions(null, x, y));
                    }
                }
                otherShelfBox.getChildren().add(new VBox(new Label(playerInfo.getNickname()) , this.otherShelf.get(l).getComponentAnchorPane(), this.otherPointsObtained.get(l).getComponentAnchorPane()));
            }
            else l--;
        }

        for(int i=ModelConstants.MAX_PLAYERS-2; i>this.guiView.gameInfo.getPlayerInfosList().size()-2;i--){
            this.otherShelf.get(i).setComponentImage(null);
        }

        this.gameContainer.setLeft(otherShelfBox);
    }

    /**
     * This method displays all the points obtained (by the player and the others
     */
    private void displayAllPointsObtained(){

        for(int i=0, l=0; i<this.guiView.gameInfo.getPlayerInfosList().size();i++,l++) {

            PlayerInfo playerInfo = this.guiView.gameInfo.getPlayerInfosList().get(i);

            Consumer<ClickableComponent> action = (component )->{
                if(playerInfo.getComGoalPoints()[0]!=0){
                    component.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(playerInfo.getComGoalPoints()[0]), 0, 0 );
                }
                else component.setComponentSavedImageFromPositions(null, 0, 0);

                if(playerInfo.getComGoalPoints()[1]!=0){
                    component.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(playerInfo.getComGoalPoints()[1]), 0, 1 );
                }
                else component.setComponentSavedImageFromPositions(null, 0, 1);

                if(playerInfo.getFirstPoint()!=0){
                    component.setComponentSavedImageFromPositions(
                            new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(
                                    "gui/images/scoring_tokens/end_game.jpg", this.getClass())), 0, 2 );
                }
                else component.setComponentSavedImageFromPositions(null, 0, 2);
            };

            //display my points
            if(playerInfo.getNickname().equals(this.guiView.myNickname)){

                action.accept(this.myPoints);

                l--;

            }
            //display other points
            else{
                action.accept(this.otherPointsObtained.get(l));
            }
        }

        //remove the other information
        for(int i=ModelConstants.MAX_PLAYERS-2; i>this.guiView.gameInfo.getPlayerInfosList().size()-2;i--){
            this.otherPointsObtained.get(i).setComponentImage(null);
        }
    }

    /**
     * This method is called from the view and displays the chat message received
     * @param message string received
     */
    public void displayMessage(String message){
        Platform.runLater(()-> this.chatLabel.setText(this.chatLabel.getText()+message+"\n"));
    }

    /**
     * This method is a utility that helps to set an image to its tile descriptor
     * @param tile corresponding tile
     * @return an image (if the tile is not empty or invalid
     */
    private Optional<Image> getImageFromTileDescription(Tile tile){
        String imageToLoad="gui/images/item_tiles/";
        switch (tile.getColor()){
            case BLUE    -> imageToLoad +="Cornici1."+tile.getSprite();
            case GREEN   -> imageToLoad +="Gatti1."  +tile.getSprite();
            case VIOLET  -> imageToLoad +="Piante1." +tile.getSprite();
            case WHITE   -> imageToLoad +="Libri1."  +tile.getSprite();
            case CYAN    -> imageToLoad +="Trofei1." +tile.getSprite();
            case YELLOW  -> imageToLoad +="Giochi1." +tile.getSprite();
            case EMPTY, INVALID -> {return Optional.empty();}
        }
        imageToLoad+=".png";
        return Optional.of(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass())));
    }

    /**
     * This method is a utility that chooses the correct background image for the common goal
     * @param integer common goal to be displayed
     * @return image referring to the correct common goal
     */
    private Image getImageFromCommonGoalDescription(Integer integer){
        String imageToLoad="gui/images/common_goal_cards/"+(integer+1)+".jpg";
        return new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass()));
    }

    /**
     * This method is a utility that chooses the correct background image for the personal
     * @param integer personal goal to be displayed
     * @return image referring to the correct personal
     */
    private Image getImageFromPersonalGoalDescription(Integer integer){
        String imageToLoad="gui/images/personal_goal_cards/Personal_Goals"+(integer+1)+".png";
        return new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass()));
    }

    /**
     * This method is a utility that chooses the correct background image for the common goal stack
     * @param integer stack value
     * @return image referring to the correct common goal stack points
     */
    private Image getImageFromCommonGoalPoints(Integer integer){
        String imageToLoad="gui/images/scoring_tokens/scoring_"+integer+".jpg";
        return new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass()));
    }

    /**
     * Method that is called whenever the mouse is clicked on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasClick(MouseEvent event){

        /*
        this.gameBoard.getTileOnComponentFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("gui/images/item_tiles/easteregg.png", this.getClass()))));
         */
        event.consume();
    }
    /**
     * Method that is called whenever the mouse is clicked on the shelf canvas
     * It takes the column where the mouse clicks and tries to send the move to the server
     * @param event mouse event
     */
    public void onMyShelfCanvasClick(MouseEvent event){

        if(!this.guiView.isMyTurn()) return;

        Optional<Integer> columnSelected = this.myShelf.getColumnOfsavedImageFromCoordinates(event.getX(),event.getY());

        if(columnSelected.isPresent()){

            try {
                this.guiView.client.makeMove(new ArrayList<>(this.positionsList.values()), columnSelected.get());
                this.errorLabel.setText("CORRECT MOVE");
            } catch (InvalidMoveException e) {
                this.errorLabel.setText("INVALID MOVE");
            } catch (InvalidNicknameException e) {
                this.errorLabel.setText("INVALID NICKNAME");
            } catch (ConnectionError e) {
                this.errorLabel.setText("CONNECTION ERROR");
            } catch (GameEndedException e) {
                this.errorLabel.setText("GAME ENDED");
            }

        }
        event.consume();
    }
    /**
     * Method that is called whenever the mouse is hovered/moved on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasHover(MouseEvent event){

        /*
        this.getTileOnGameBoardFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/item_tiles/easteregg2.png", this.getClass()))));
        */
        event.consume();
    }

    /**
     * This method checks if a drag is started on the game board
     * If there is, it takes the image and the position and adds it to a clipboard, which lets the user drag the tile on the screen
     * @param event mouse dragged event
     */
    @FXML
    protected void onGameBoardCanvasDragDetected(MouseEvent event){
        if(!this.guiView.isMyTurn()) return;
        Optional<ImageView> imagePressed = gameBoard.getTileOnComponentFromPosition(event.getX(), event.getY());

        Optional<Position> positionPressed = gameBoard.getPositionOfSavedImageFromCoordinates(event.getX(), event.getY());

        imagePressed.ifPresent((imageView)->{

            if(!this.guiView.checkValidPosition(this.positionsList.values().stream().toList(), positionPressed.get())){
                this.errorLabel.setText("SELECT A VALID TILE BOZO");
                return;
            }
            this.errorLabel.setText("GOOD BOY");

            Dragboard db=imageView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(this.scaleImage(imageView.getImage(), imageView.getFitWidth(), imageView.getFitHeight()));
            //content.putImage(imageView.getImage());
            content.putString("image moved");
            content.put(this.dft, positionPressed.get());
            db.setContent(content);
        });
        event.consume();
    }

    /**
     * This method is called whenever the drag enters the move list, in order to set that image as a "target" where the move can be dropped
     * @param event mouse event
     */
    @FXML
    protected void onMoveListCanvasDragOver(DragEvent event){
        if(!this.guiView.isMyTurn()) return;
        if(event.getDragboard().hasImage()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    /**
     * This method is called when the drag on the move list canvas is dropped.
     * It sets the move in the correct position based on where it dropped.
     * The information is taken from what was moved on the clipboard
     * @param event mouse event
     */
    @FXML
    protected void onMoveListCanvasDragDropped(DragEvent event){
        if(!this.guiView.isMyTurn()) return;
        Optional<Position> index=this.moveList.getPositionOfSavedImageFromCoordinates(event.getX(),event.getY());
        if(index.isEmpty()){
            event.setDropCompleted(true);
            event.consume();
            return;
        }
        Dragboard db=event.getDragboard();
        if(db.hasImage()) {
            Position movePosition = (Position)db.getContent(this.dft);

            moveList.setComponentSavedImageFromCoordinates(this.getImageFromTileDescription(this.guiView.gameInfo.getGameBoard()[movePosition.y()][movePosition.x()]).get(), event.getX(), event.getY());

            this.positionsList.put(index.get().x(), movePosition);

            event.setDropCompleted(true);
        }
        else event.setDropCompleted(false);
        event.consume();
    }

    /**
     * This method is called whenever the move list canvas is clicked. It lets the user cancel a tile from the current move
     * @param event mouse event
     */
    @FXML
    protected void onMoveListCanvasClick(MouseEvent event){
        if(!this.guiView.isMyTurn()) return;

        Optional<Position> index=this.moveList.getPositionOfSavedImageFromCoordinates(event.getX(),event.getY());
        if(index.isEmpty()) return;

        this.moveList.setComponentSavedImageFromCoordinates(null, event.getX(), event.getY());

        this.positionsList.remove(index.get().x());

        event.consume();

    }

    /**
     * This method is called whenever the chat button is clicked.
     * It sends the message to the server in all chat  TODO implement private chat
     */
    @FXML
    protected void onChatButtonMouseClick(){
        if(this.chatTextField.getText().isEmpty()) return;

        try {
            this.guiView.client.messageAll(this.chatTextField.getText());
        } catch (ConnectionError e) {
            this.errorLabel.setText("CONNECTION ERROR");
        }
    }

    /**
     * This method is called whenever the refresh button is clicked.
     * It displays all the information received again
     */
    @FXML
    protected void onRefreshButtonMouseClick(){
        Platform.runLater(this::drawClickableComponents);
        Platform.runLater(this::display);
    }

    /**
     * This method takes an image and scales it to the desired size
     * @param source image to be scaled
     * @param targetWidth target width of the image
     * @param targetHeight target height of the image
     * @return the scaled image
     */
    private Image scaleImage(Image source, double targetWidth, double targetHeight) {
        ImageView imageView = new ImageView(source);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }

    /**
     * This method is called whenever the game is ended.
     * It pops an alert to the screen showing the leaderboard
     */
    private void showGameEndedAlert(){

        Platform.runLater(()-> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game ended");
            alert.setHeaderText("LeaderBoard:");
            alert.setContentText("No leaderboard yet");

            alert.showAndWait();
        });

    }

    /**
     * This method is called whenever an error client side has occurred.
     * It pops an alert to the screen showing the error message
     * @param message error message to be shown
     */
    public void showErrorAlert(String message){

        Platform.runLater(()->{
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something went wrong");
            alert.setContentText(message);

            alert.showAndWait();
        });

    }

}
