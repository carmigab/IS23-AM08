package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.utilities.UtilityFunctions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameViewController implements Initializable{
    @FXML
    private ImageView gameBoard;
    @FXML
    private AnchorPane anchorPane;

    private GameInfo currentInfo;

    private final Integer tileX=30;
    private final Integer tileY=30;

    private List<ImageView> tilesOnGameBoard;

    private void display(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentInfo=null;

        this.tilesOnGameBoard=new ArrayList<>();

        this.anchorPane.getChildren().add(this.gameBoard);

        int xoff=0;
        int yoff=0;

        for(int i=0; i< ModelConstants.BOARD_DIMENSION;i++) {
            for (int j = 0; j < ModelConstants.BOARD_DIMENSION; j++) {
                ImageView image = new ImageView();
                this.anchorPane.getChildren().add(image);
                this.tilesOnGameBoard.add(image);
                image.setVisible(true);
                image.setImage(new Image(UtilityFunctions.getInputStreamFromFileNameRelativePath("images/item_tiles/Cornici1.1.png", this.getClass())));
                image.setFitHeight(this.tileY);
                image.setFitWidth(this.tileX);
                image.setLayoutX(xoff);
                image.setLayoutY(yoff);
                xoff = this.tileX * j;
            }
            yoff = this.tileY * i;
        }

        this.display();
    }
}
