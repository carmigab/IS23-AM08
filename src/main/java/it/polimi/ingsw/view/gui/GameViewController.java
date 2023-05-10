package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.utilities.UtilityFunctions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

public class GameViewController implements Initializable{
    /**
     * This attribute stores the image information of the game board
     */
    @FXML
    private ImageView gameBoard;
    /**
     * This attribute stores the panel used to contain all the components of the the game board (image, all the tiles, canvas)
     */
    @FXML
    private AnchorPane anchorPane;
    /**
     * This attribute stores the canvas put on top of the game board used for the recognition of the mouse interactions
     */
    @FXML
    private Canvas gameBoardCanvas;
    /**
     * Game info of the current position
     */
    private GameInfo currentInfo;

    /**
     * Percentual offset between the edge of the game board and the first tile
     */
    private final Double tileGameBoardOffset=0.045;
    /**
     * Percentual offset between two tiles (only one side)
     */
    private final Double tileGameBoardDistance=this.tileGameBoardOffset/3;
    /**
     * Width dimension of the tile
     */
    private Double tileX;
    /**
     * Height dimension of the tile
     */
    private Double tileY;

    /**
     * All the tiles contained in the game board (also contains invalid)
     */
    private List<ImageView> tilesOnGameBoard;

    private void display(){

    }

    /**
     * Method that is called when the fxml file is loaded
     * It setups all the game board for a correct showing of the images
     * @param url something
     * @param resourceBundle something else
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentInfo=null;

        this.tilesOnGameBoard=new ArrayList<>();

        this.anchorPane.getChildren().add(this.gameBoard);

        this.calculateCurrentTileDimension();

        //we start at once the offset and once the distance between the tiles
        double xoff=this.gameBoard.getFitWidth()*(this.tileGameBoardOffset+this.tileGameBoardDistance);
        double yoff=this.gameBoard.getFitHeight()*(this.tileGameBoardOffset+this.tileGameBoardDistance);

        for(int i=0; i< ModelConstants.BOARD_DIMENSION;i++) {
            for (int j = 0; j < ModelConstants.BOARD_DIMENSION; j++) {
                ImageView image = new ImageView();
                //add the image to the pane so its offsets will be calulated with relative values
                this.anchorPane.getChildren().add(image);
                this.tilesOnGameBoard.add(image);
                image.setVisible(true);
                image.setImage(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/item_tiles/Cornici1.1.png", this.getClass())));
                //set the dimensions
                image.setFitHeight(this.tileY);
                image.setFitWidth(this.tileX);
                //set the position in the pane
                image.setLayoutX(xoff);
                image.setLayoutY(yoff);
                //increase the x offset by the dimension and two distances between tiles
                xoff += this.tileX + this.gameBoard.getFitHeight()*this.tileGameBoardDistance*2;
            }
            //reset x
            xoff = this.gameBoard.getFitWidth()*(this.tileGameBoardOffset+this.tileGameBoardDistance);
            //increase the y offset the same way
            yoff += this.tileY + this.gameBoard.getFitHeight()*this.tileGameBoardDistance*2;
        }

        //resize the canvas to match the game board
        this.gameBoardCanvas.setWidth (this.gameBoard.getFitWidth() );
        this.gameBoardCanvas.setHeight(this.gameBoard.getFitHeight());
        //add the canvas to the pane
        this.anchorPane.getChildren().add(this.gameBoardCanvas);
        //set it to the top of the stack so it can be clicked
        this.gameBoardCanvas.toFront();

        this.display();
    }

    /**
     * Method that is called whenever the mouse is clicked on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasClick(MouseEvent event){

        this.getTileOnGameBoardFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/easteregg.png", this.getClass()))));

    }
    /**
     * Method that is called whenever the mouse is hovered/moved on the game board canvas
     * @param event mouse event
     */
    public void onGameBoardCanvasHover(MouseEvent event){

        this.getTileOnGameBoardFromPosition(event.getX(), event.getY()).ifPresent(
                imageView -> imageView.setImage(
                        new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/easteregg2.png", this.getClass()))));

    }

    /**
     * This method obtains from the current dimensions of the game board the size of the tile (note that it is a square so it is the same x and y)
     * To obtain the formula we use this reasoning:
     * Let x be our dimension to find
     * Let l be the total length of the gameBoard
     * Let n be the total number of tiles per row/column
     * Let o% be the offset from one edge to the first tile in the board (in percentage values, note that the image has some offset between the left most tile and the edge of the image)
     * Let d% be the space in betweeen two tiles (in percentage values, calculated once, we do *2 in the formula)
     * Then o = o% * l and d = d% * l
     * So to get the length of the tile without any space in between (called y) we do
     * y = (l - 2o) / n -> (factor l) y = l * (1 - 2o%) / n
     * And to get the tile with spaces we simply do
     * x = y - 2d -> (factor l) x = l * ((1 - 2o%) / n - 2d%)
     */
    private void calculateCurrentTileDimension(){
        this.tileX=this.gameBoard.getFitWidth() *((1-2*this.tileGameBoardOffset)/ModelConstants.BOARD_DIMENSION-2*this.tileGameBoardDistance);
        this.tileY=this.gameBoard.getFitHeight()*((1-2*this.tileGameBoardOffset)/ModelConstants.BOARD_DIMENSION-2*this.tileGameBoardDistance);
    }

    /**
     * Method that given two coordinates if they are in between the dimensions of a tile it returns it
     * @param x x coordinate
     * @param y y coordinate
     * @return an image, if present
     */
    private Optional<ImageView> getTileOnGameBoardFromPosition(double x, double y){

        return this.tilesOnGameBoard.stream()
                .filter(imageView -> x >= imageView.getLayoutX() && x <= imageView.getLayoutX()+imageView.getFitWidth() &&
                                     y >= imageView.getLayoutY() && y <= imageView.getLayoutY()+imageView.getFitHeight() )
                .findFirst();
    }
}
