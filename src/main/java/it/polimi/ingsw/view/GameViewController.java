package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.utilities.UtilityFunctions;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.ClickableComponent;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class GameViewController implements Initializable{
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
    @FXML
    private ImageView myShelfImage;
    @FXML
    private AnchorPane myShelfAnchorPane;
    @FXML
    private Canvas myShelfCanvas;
    @FXML
    private ImageView commonGoal1Image;
    @FXML
    private AnchorPane commonGoal1AnchorPane;
    @FXML
    private Canvas commonGoal1Canvas;
    @FXML
    private ImageView commonGoal2Image;
    @FXML
    private AnchorPane commonGoal2AnchorPane;
    @FXML
    private Canvas commonGoal2Canvas;
    @FXML
    private ImageView personalGoalImage;
    @FXML
    private AnchorPane personalGoalAnchorPane;
    @FXML
    private Canvas personalGoalCanvas;
    @FXML
    private ImageView moveListImage;
    @FXML
    private AnchorPane moveListAnchorPane;
    @FXML
    private Canvas moveListCanvas;
    /**
     * This attribute stores the canvas put on top of the game board used for the recognition of the mouse interactions
     */
    @FXML
    private Canvas gameBoardCanvas;
    /**
     * This attribute stores the container where all the scene is set
     */
    @FXML
    private VBox gameContainer;


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



    private ClickableComponent gameBoard;
    private ClickableComponent myShelf;
    private ClickableComponent commonGoal1;
    private ClickableComponent commonGoal2;
    private ClickableComponent personalGoal;
    private ClickableComponent moveList;

    private List<Position> positionsList;

    private View guiView;

    public void setGuiView(View guiView){
        this.guiView=guiView;
    }


    /**
     * Method that is called when the fxml file is loaded
     * It setups all the game board for a correct showing of the images
     * @param url something
     * @param resourceBundle something else
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.positionsList=new ArrayList<>(ModelConstants.MAX_NUM_OF_MOVES);

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


        this.gameBoard.draw();
        this.myShelf.draw();
        this.commonGoal1.draw();
        this.commonGoal2.draw();
        this.personalGoal.draw();
        this.moveList.draw();

        //Prepare the listener of the resize event
        ChangeListener<Number> onDimensionsChange = (observable, oldValue, newValue) ->
                {
                    this.gameBoard.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.myShelf.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.commonGoal1.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.commonGoal2.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.personalGoal.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.moveList.setComponentDimensions(Math.min(gameContainer.getWidth(), gameContainer.getHeight()));
                    this.gameBoard.draw();
                    this.myShelf.draw();
                    this.commonGoal1.draw();
                    this.commonGoal2.draw();
                    this.personalGoal.draw();
                    this.moveList.draw();
                };

        //Add the listeners
        this.gameContainer.widthProperty().addListener(onDimensionsChange);
        this.gameContainer.heightProperty().addListener(onDimensionsChange);

        //node, column, row, colspan, rowspan
        this.gridPane.add(this.gameBoardAnchorPane   , 0, 0, 1, 2);
        this.gridPane.add(this.myShelfAnchorPane     , 1, 0, 1, 2);
        this.gridPane.add(this.commonGoal1AnchorPane , 0, 2, 1, 1);
        this.gridPane.add(this.commonGoal2AnchorPane , 0, 3, 1, 1);
        this.gridPane.add(this.personalGoalAnchorPane, 2, 0, 1, 1);
        this.gridPane.add(this.moveListAnchorPane    , 2, 2, 1, 1);

        this.gridPane.setHgap(20);
        this.gridPane.setVgap(20);

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
     * This method takes the current information from the game and draws it in the container
     */
    public void display(){

        if (this.guiView == null) return;

        if(this.guiView.gameInfo == null) return;


        for(int i=0; i<ModelConstants.BOARD_DIMENSION;i++){
            for(int j=0;j<ModelConstants.BOARD_DIMENSION;j++){
                Optional<Image> image=this.getImageFromTileDescription(this.guiView.gameInfo.getGameBoard()[i][j]);
                int x=i;
                int y=j;
                image.ifPresentOrElse((smth)->this.gameBoard.setComponentSavedImageFromPositions(image.get(), x, y),
                        ()->this.gameBoard.setComponentSavedImageFromPositions(null, x, y));
            }
        }

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

        this.commonGoal1.setComponentImage(this.getImageFromCommonGoalDescription(this.guiView.gameInfo.getCommonGoalsCreated().get(0)));
        this.commonGoal2.setComponentImage(this.getImageFromCommonGoalDescription(this.guiView.gameInfo.getCommonGoalsCreated().get(1)));

        this.commonGoal1.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(this.guiView.gameInfo.getCommonGoalsStack().get(0)),0,0);
        this.commonGoal2.setComponentSavedImageFromPositions(this.getImageFromCommonGoalPoints(this.guiView.gameInfo.getCommonGoalsStack().get(1)),0,0);

        this.positionsList.clear();
        for(int i=0;i<ModelConstants.MAX_NUM_OF_MOVES;i++){
            this.moveList.setComponentSavedImageFromPositions(null, 0, i);
        }
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

    private Image getImageFromCommonGoalDescription(Integer integer){
        String imageToLoad="gui/images/common_goal_cards/"+(integer+1)+".jpg";
        return new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass()));
    }

    private Image getImageFromCommonGoalPoints(Integer integer){
        String imageToLoad="gui/images/scoring_tokens/scoring_"+integer+".jpg";
        return new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass()));
    }

    /**
     * Method that is called whenever the mouse is clicked on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasClick(MouseEvent event){


        this.gameBoard.getTileOnComponentFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("gui/images/item_tiles/easteregg.png", this.getClass()))));

    }
    /**
     * Method that is called whenever the mouse is clicked on the game board canvas
     * @param event mouse event
     */
    public void onMyShelfCanvasClick(MouseEvent event){

        /*
        this.myShelf.getTileOnComponentFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("gui/images/item_tiles/easteregg.png", this.getClass()))));
         */
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
    }

    private final DataFormat dft=new DataFormat("gameBoardPosition");

    @FXML
    protected void onGameBoardCanvasDragDetected(MouseEvent event){
        if(!this.guiView.isMyTurn()) return;
        Optional<ImageView> imagePressed = gameBoard.getTileOnComponentFromPosition(event.getX(), event.getY());

        Optional<Position> positionPressed = gameBoard.getPositionOfSavedImageFromCoordinates(event.getX(), event.getY());


        imagePressed.ifPresent((imageView)->{
            Dragboard db=imageView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            ClipboardContent content = new ClipboardContent();
            //content.putImage(scaledImage);
            content.putImage(imageView.getImage());
            content.putString("image moved");
            content.put(this.dft, positionPressed.get());
            db.setContent(content);
            event.consume();
        });
    }

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
            moveList.setComponentSavedImageFromCoordinates(db.getImage(), event.getX(), event.getY());
            Position movePosition = (Position)db.getContent(this.dft);

            this.positionsList.add(index.get().x(), movePosition);

            event.setDropCompleted(true);
        }
        else event.setDropCompleted(false);
        event.consume();
    }

    @FXML
    protected void onMoveListCanvasDragOver(DragEvent event){
        if(!this.guiView.isMyTurn()) return;
        if(event.getDragboard().hasImage()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    private Image scaleImage(Image source, double targetWidth, double targetHeight) {
        ImageView imageView = new ImageView(source);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }
}
