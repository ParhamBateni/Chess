package Model;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.logging.Handler;

public class Cell implements EventHandler {
    private Piece piece;
    private boolean isSelected;
    private StackPane StackPane;
    public Cell(StackPane StackPane){
        this.StackPane=StackPane;
        this.isSelected=false;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        StackPane.getChildren().add(piece.image);
        piece.image.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ImageView image= (ImageView) mouseEvent.getSource();
                Rectangle rectangle=(Rectangle) image.getParent().getChildrenUnmodifiable().get(0);
                rectangle.opacityProperty().set(1);
                rectangle.setFill(Color.CHARTREUSE);
            }
        });
    }


    @Override
    public void handle(Event event) {
        System.out.println("we are here");
    }
}
