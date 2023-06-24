package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Position;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is a utility used for wrapping a lot of gui components into a single big component that can be resized, clicked and performed actions on
 */
public class   ClickableComponent {
    /**
     * Attribute containing the background image of the component
     */
    private final ImageView componentImage;
    /**
     * Attribute containing the anchor pane of the component
     */
    private final AnchorPane componentAnchorPane;
    /**
     * Attribute containing the canvas of the component
     */
    private final Canvas componentCanvas;
    /**
     * Attribute containing all the images contained in the component
     */
    private final List<ImageView> componentSavedImages;

    /**
     * Attribute containing all the setup of the clickable component
     */
    private final ClickableComponentSetup setup;

    /**
     * Width dimension of the single tile
     */
    private Double tileX;
    /**
     * Height dimension of the single tile
     */
    private Double tileY;


    /**
     * Constructor of the component
     * @param componentImage background image
     * @param componentAnchorPane anchor pane
     * @param componentCanvas canvas
     * @param setup options of the component
     */
    public ClickableComponent(ImageView componentImage, AnchorPane componentAnchorPane, Canvas componentCanvas, ClickableComponentSetup setup){
        this.componentImage=componentImage;
        this.componentAnchorPane=componentAnchorPane;
        this.componentCanvas=componentCanvas;
        this.componentSavedImages=new ArrayList<>();
        this.setup=setup;
        this.tileX=0.0;
        this.tileY=0.0;
        this.initializeComponent();
    }

    /**
     * This method attaches every node to the anchor pane
     * It also creates the images contained in the component
     */
    private void initializeComponent(){

        //Add the component to the anchor pane
        this.componentAnchorPane.getChildren().add(this.componentImage);

        //Create all the tiles
        for(int i=0; i< this.setup.getComponentSavedImagesX();i++) {
            for (int j = 0; j < this.setup.getComponentSavedImagesY(); j++) {
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

    /**
     * This method draws each node in the component with its relative dimensions
     */
    public void draw(){
        this.calculateCurrentTileDimension();

        //we start at once the offset and once the distance between the tiles
        double xoff=this.componentImage.getFitWidth()* (this.setup.getTileComponentOffsetXLeft()+this.setup.getTileComponentDistanceX());
        double yoff=this.componentImage.getFitHeight()*(this.setup.getTileComponentOffsetYUp()+this.setup.getTileComponentDistanceY());

        for(int i = 0; i< this.setup.getComponentSavedImagesX(); i++) {
            for (int j = 0; j < this.setup.getComponentSavedImagesY(); j++) {
                ImageView image = this.componentSavedImages.get(i*this.setup.getComponentSavedImagesY()+j);
                //set the dimensions
                image.setFitHeight(this.tileY);
                image.setFitWidth(this.tileX);
                //set the position in the pane
                image.setLayoutX(xoff);
                image.setLayoutY(yoff);
                //increase the x offset by the dimension and two distances between tiles
                xoff += this.tileX + this.componentImage.getFitWidth()*this.setup.getTileComponentDistanceX()*2;
            }
            //reset x
            xoff = this.componentImage.getFitWidth()* (this.setup.getTileComponentOffsetXLeft()+this.setup.getTileComponentDistanceX());
            //increase the y offset the same way
            yoff += this.tileY + this.componentImage.getFitHeight()*this.setup.getTileComponentDistanceY()*2;
        }

        //resize the canvas to match the game board
        this.componentCanvas.setLayoutX(this.componentImage.getFitWidth() *this.setup.getTileComponentOffsetXLeft());
        this.componentCanvas.setLayoutY(this.componentImage.getFitHeight()*this.setup.getTileComponentOffsetYUp());

        this.componentCanvas.setWidth (this.componentImage.getFitWidth() *(1-(this.setup.getTileComponentOffsetXLeft()+this.setup.getTileComponentOffsetXRight())));
        this.componentCanvas.setHeight(this.componentImage.getFitHeight()*(1-(this.setup.getTileComponentOffsetYUp()+this.setup.getTileComponentOffsetYDown())));

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
        this.tileX=this.componentImage.getFitWidth() *((1-(this.setup.getTileComponentOffsetXLeft()+this.setup.getTileComponentOffsetXRight()))/this.setup.getComponentSavedImagesY()-2*this.setup.getTileComponentDistanceX());
        this.tileY=this.componentImage.getFitHeight()*((1-(this.setup.getTileComponentOffsetYUp()+this.setup.getTileComponentOffsetYDown()))/this.setup.getComponentSavedImagesX()-2* setup.getTileComponentDistanceY());
    }

    /**
     * This method resizes the game board to a square which length is passed in input
     * @param dim length of the dimenison of the game board
     */
    public void setComponentDimensions(Double dim){
        dim*=this.setup.getComponentPredefinedRatio();
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

        Optional<Position> position=this.getPositionOfSavedImageFromCoordinates(x,y);

        return position.map(value -> this.componentSavedImages.get(value.y() * this.setup.getComponentSavedImagesY() + value.x()));
    }

    /**
     * Method that given two coordinates, if they are in between the dimensions of a tile, it returns the position of the specified tile
     * @param x x coordinate
     * @param y y coordinate
     * @return a position, if present
     */
    public Optional<Position> getPositionOfSavedImageFromCoordinates(double x, double y){

        //The values are relative to the canvas size, so we need to adapt them to our needs
        Double realX=x+this.setup.getTileComponentOffsetXLeft()*this.componentImage.getFitWidth();
        Double realY=y+this.setup.getTileComponentOffsetYUp()*this.componentImage.getFitHeight();

        return this.componentSavedImages.stream()
                .filter(imageView -> realX >= imageView.getLayoutX() && realX <= imageView.getLayoutX()+imageView.getFitWidth() &&
                        realY >= imageView.getLayoutY() && realY <= imageView.getLayoutY()+imageView.getFitHeight() )
                .map(imageView ->
                        new Position(   Double.valueOf((imageView.getLayoutX()+imageView.getFitWidth()/2)/(this.componentImage.getFitWidth()/this.setup.getComponentSavedImagesY())).intValue(),
                                Double.valueOf((imageView.getLayoutY()+imageView.getFitHeight()/2)/(this.componentImage.getFitHeight()/this.setup.getComponentSavedImagesX())).intValue())
                ).findFirst();
    }

    /**
     * Method that given two coordinates if they are in between the dimensions of a tile it returns its column
     * @param x x coordinate
     * @param y y coordinate
     * @return an integer, if present
     */
    public Optional<Integer> getColumnOfsavedImageFromCoordinates(double x, double y){
        //The values are relative to the canvas size, so we need to adapt them to our needs
        Double realX=x+this.setup.getTileComponentOffsetXLeft()*this.componentImage.getFitWidth();
        Double realY=y+this.setup.getTileComponentOffsetYUp()*this.componentImage.getFitHeight();

        return this.componentSavedImages.stream()
                .filter(imageView -> realX >= imageView.getLayoutX() && realX <= imageView.getLayoutX()+imageView.getFitWidth() &&
                        realY >= imageView.getLayoutY() && realY <= imageView.getLayoutY()+imageView.getFitHeight() )
                .map(imageView ->
                        (int)((imageView.getLayoutX()+imageView.getFitWidth()/2)/(this.componentImage.getFitWidth()/this.setup.getComponentSavedImagesY()))
                        ).findFirst();
    }


    /**
     * Setter of the background image
     * @param image image to be set as background
     */
    public void setComponentImage(Image image){
        this.componentImage.setImage(image);
    }

    /**
     * Setter of the single image
     * @param image image to be set
     * @param x x position of the image
     * @param y y position of the image
     */
    public void setComponentSavedImageFromPositions(Image image, int x, int y){
        this.componentSavedImages.get(x*this.setup.getComponentSavedImagesY()+y).setImage(image);
    }

    /**
     * Setter of the single image
     * @param image image to be set
     * @param x x coordinate of the image
     * @param y y coordinate of the image
     */
    public void setComponentSavedImageFromCoordinates(Image image, double x, double y){
        Optional<ImageView> imageView =this.getTileOnComponentFromPosition(x,y);

        imageView.ifPresent( (imageView1)-> imageView1.setImage(image));
    }

    /**
     * Getter of the component's anchor pane
     * @return an anchor pane
     */
    public AnchorPane getComponentAnchorPane(){
        return this.componentAnchorPane;
    }


    /*
    public void setTileComponentOffsetXLeft(Double tileComponentOffsetXLeft) {
        this.tileComponentOffsetXLeft = tileComponentOffsetXLeft;
    }

    public void setTileComponentOffsetXRight(Double tileComponentOffsetXRight) {
        this.tileComponentOffsetXRight = tileComponentOffsetXRight;
    }

    public void setTileComponentOffsetYUp(Double tileComponentOffsetYUp) {
        this.tileComponentOffsetYUp = tileComponentOffsetYUp;
    }

    public void setTileComponentOffsetYDown(Double tileComponentOffsetYDown) {
        this.tileComponentOffsetYDown = tileComponentOffsetYDown;
    }

    public void setTileComponentDistanceX(Double tileComponentDistanceX) {
        this.tileComponentDistanceX = tileComponentDistanceX;
    }

    public void setTileComponentDistanceY(Double tileComponentDistanceY) {
        this.tileComponentDistanceY = tileComponentDistanceY;
    }
     */

}
