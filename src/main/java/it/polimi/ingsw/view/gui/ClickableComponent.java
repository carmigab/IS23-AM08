package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClickableComponent {

    private final ImageView componentImage;
    private final AnchorPane componentAnchorPane;
    private final Canvas componentCanvas;
    private final List<ImageView> componentSavedImages;
    private final Integer componentSavedImagesX;
    private final Integer componentSavedImagesY;
    private Double tileX;
    private Double tileY;
    /**
     * Percentage offset between the edge of the game board and the first tile
     */
    private Double tileComponentOffset=0.045;
    /**
     * Percentage offset between two tiles (only one side)
     */
    private Double tileComponentDistance=this.tileComponentOffset/3;
    /**
     * Percantage ratio between the container dimensions (the smaller one) and the size of the game board
     */
    private Double componentPredefinedRatio = 0.5;

    public ClickableComponent(ImageView componentImage, AnchorPane componentAnchorPane, Canvas componentCanvas, Integer componentSavedImagesX, Integer componentSavedImagesY, Double tileComponentOffset, Double tileComponentDistance, Double componentPredefinedRatio){
        this(componentImage, componentAnchorPane, componentCanvas, componentSavedImagesX, componentSavedImagesY);
        this.tileComponentOffset=tileComponentOffset;
        this.tileComponentDistance=tileComponentDistance;
        this.componentPredefinedRatio=componentPredefinedRatio;
    }


    public ClickableComponent(ImageView componentImage, AnchorPane componentAnchorPane, Canvas componentCanvas, Integer componentSavedImagesX, Integer componentSavedImagesY){
        this.componentImage=componentImage;
        this.componentAnchorPane=componentAnchorPane;
        this.componentCanvas=componentCanvas;
        this.componentSavedImages=new ArrayList<>();
        this.componentSavedImagesX=componentSavedImagesX;
        this.componentSavedImagesY=componentSavedImagesY;
        this.tileX=0.0;
        this.tileY=0.0;
        this.initializeComponent();
    }
    private void initializeComponent(){

        //Add the component to the anchor pane
        this.componentAnchorPane.getChildren().add(this.componentImage);

        //Create all the tiles
        for(int i=0; i< this.componentSavedImagesX;i++) {
            for (int j = 0; j < this.componentSavedImagesY; j++) {
                ImageView image = new ImageView();
                //add the image to the pane so its offsets will be calulated with relative values
                this.componentAnchorPane.getChildren().add(image);
                this.componentSavedImages.add(image);
                image.setVisible(true);
            }
        }

        //add the canvas to the anchor pane
        this.componentAnchorPane.getChildren().add(this.componentCanvas);
        //set it to the top of the stack, so it can be clicked
        this.componentCanvas.toFront();

    }

    public void draw(){
        this.calculateCurrentTileDimension();

        //we start at once the offset and once the distance between the tiles
        double xoff=this.componentImage.getFitWidth()* (this.tileComponentOffset+this.tileComponentDistance);
        double yoff=this.componentImage.getFitHeight()*(this.tileComponentOffset+this.tileComponentDistance);

        for(int i = 0; i< this.componentSavedImagesX; i++) {
            for (int j = 0; j < this.componentSavedImagesY; j++) {
                ImageView image = this.componentSavedImages.get(i*this.componentSavedImagesY+j);
                //set the dimensions
                image.setFitHeight(this.tileY);
                image.setFitWidth(this.tileX);
                //set the position in the pane
                image.setLayoutX(xoff);
                image.setLayoutY(yoff);
                //increase the x offset by the dimension and two distances between tiles
                xoff += this.tileX + this.componentImage.getFitWidth()*this.tileComponentDistance*2;
            }
            //reset x
            xoff = this.componentImage.getFitWidth()*(this.tileComponentOffset+this.tileComponentDistance);
            //increase the y offset the same way
            yoff += this.tileY + this.componentImage.getFitHeight()*this.tileComponentDistance*2;
        }

        //resize the canvas to match the game board
        this.componentCanvas.setWidth (this.componentImage.getFitWidth() );
        this.componentCanvas.setHeight(this.componentImage.getFitHeight());
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
        this.tileX=this.componentImage.getFitWidth() *((1-2*this.tileComponentOffset)/this.componentSavedImagesY-2*this.tileComponentDistance);
        this.tileY=this.componentImage.getFitHeight()*((1-2*this.tileComponentOffset)/this.componentSavedImagesY-2*this.tileComponentDistance);
    }

    /**
     * This method resizes the game board to a square which length is passed in input
     * @param dim length of the dimenison of the game board
     */
    public void setComponentDimensions(Double dim){
        dim*=this.componentPredefinedRatio;
        this.componentImage.setFitWidth(dim);
        this.componentImage.setFitHeight(dim);
    }

    /**
     * Method that given two coordinates if they are in between the dimensions of a tile it returns it
     * @param x x coordinate
     * @param y y coordinate
     * @return an image, if present
     */
    public Optional<ImageView> getTileOnComponentFromPosition(double x, double y){

        return this.componentSavedImages.stream()
                .filter(imageView -> x >= imageView.getLayoutX() && x <= imageView.getLayoutX()+imageView.getFitWidth() &&
                        y >= imageView.getLayoutY() && y <= imageView.getLayoutY()+imageView.getFitHeight() )
                .findFirst();
    }
}
