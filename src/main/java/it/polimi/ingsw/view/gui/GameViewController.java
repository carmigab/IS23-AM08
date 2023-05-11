package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utilities.UtilityFunctions;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class GameViewController implements Initializable{
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
    /**
     * This attribute stores the canvas put on top of the game board used for the recognition of the mouse interactions
     */
    @FXML
    private Canvas gameBoardCanvas;
    /**
     * This attribute stores the container where all the scene is set
     */
    @FXML
    private VBox container;
    /**
     * Game info of the current position
     */
    private GameInfo gameInfo;

    private ClickableComponent gameBoard;
    private ClickableComponent myShelf;


    /**
     * Method that is called when the fxml file is loaded
     * It setups all the game board for a correct showing of the images
     * @param url something
     * @param resourceBundle something else
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameInfo=null;

        //Create the array of tiles
        this.gameBoard=new ClickableComponent(this.gameBoardImage, this.gameBoardAnchorPane, this.gameBoardCanvas, ModelConstants.BOARD_DIMENSION, ModelConstants.BOARD_DIMENSION);
        //Create the array of tiles
        this.myShelf=new ClickableComponent(this.myShelfImage, this.myShelfAnchorPane, this.myShelfCanvas, ModelConstants.ROWS_NUMBER, ModelConstants.COLS_NUMBER);

        this.gameBoard.draw();
        this.myShelf.draw();

        this.display();

        //Prepare the listener of the resize event
        ChangeListener<Number> onDimensionsChange = (observable, oldValue, newValue) ->
                {
                    this.gameBoard.setComponentDimensions(Math.min(container.getWidth(), container.getHeight()));
                    this.myShelf.setComponentDimensions(Math.min(container.getWidth(), container.getHeight()));
                    this.gameBoard.draw();
                    this.myShelf.draw();
                };

        //Add the listeners
        this.container.widthProperty().addListener(onDimensionsChange);
        this.container.heightProperty().addListener(onDimensionsChange);
    }

    /**
     * This method takes the current information from the game and draws it in the container
     */
    private void display(){
        if(gameInfo == null) return;

        /*
        for(int i=0; i<ModelConstants.BOARD_DIMENSION;i++){
            for(int j=0;j<ModelConstants.BOARD_DIMENSION;j++){
                ImageView image = this.tilesOnGameBoard.get(i*ModelConstants.BOARD_DIMENSION+j);
                this.setImageFromTileDescription(this.tilesOnGameBoard.get(i*ModelConstants.BOARD_DIMENSION+j),this.gameInfo.getGameBoard()[i][j]);
            }
        }
        */
    }

    /**
     * This method is a utility that helps to set an image to its tile descriptor
     * @param image image to be modified
     * @param tile corresponding tile
     */
    private void setImageFromTileDescription(ImageView image, Tile tile){
        String imageToLoad="images/item_tiles/";
        switch (tile.getColor()){
            case BLUE    -> imageToLoad +="Cornici1."+tile.getSprite();
            case GREEN   -> imageToLoad +="Gatti1."  +tile.getSprite();
            case VIOLET  -> imageToLoad +="Piante1." +tile.getSprite();
            case WHITE   -> imageToLoad +="Libri1."  +tile.getSprite();
            case CYAN    -> imageToLoad +="Trofei1." +tile.getSprite();
            case YELLOW  -> imageToLoad +="Giochi1." +tile.getSprite();
            case EMPTY   -> imageToLoad +="easteregg";
            case INVALID -> {return;}
        }
        imageToLoad+=".png";
        image.setImage(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath(imageToLoad, this.getClass())));
    }

    /**
     * Method that is called whenever the mouse is clicked on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasClick(MouseEvent event){

        this.gameBoard.getTileOnComponentFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/item_tiles/easteregg.png", this.getClass()))));

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

}
