package Controller;

import View.Popup;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ResignController {
    public Button yesButton;
    public Button noButton;
    public void yesEntered(MouseEvent mouseEvent) {
        yesButton.setStyle("-fx-font-weight: bold");
        yesButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE, CornerRadii.EMPTY, Insets.EMPTY)));
        yesButton.setTextFill(Color.BLACK);
    }

    public void noEntered(MouseEvent mouseEvent) {
        noButton.setStyle("-fx-font-weight: bold");
        noButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE, CornerRadii.EMPTY, Insets.EMPTY)));
        noButton.setTextFill(Color.BLACK);
    }

    public void yesExited(MouseEvent mouseEvent) {
        yesButton.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
        yesButton.setTextFill(Color.CHARTREUSE);
    }

    public void noExited(MouseEvent mouseEvent) {
        noButton.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
        noButton.setTextFill(Color.CHARTREUSE);
    }

}
