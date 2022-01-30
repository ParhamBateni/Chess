package Controller;

import Model.Game;
import View.GameMenu;
import View.Popup;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PopMenuController implements Initializable{
    public Button playAgainButton;
    public Button exitButton;
    public Label message;

    public void playAgainClicked(){
        Popup.popupStage.close();
        GameMenu.gameMenuStage.close();
        try {
            new GameMenu().start(new Stage());
        } catch (Exception e) {
        }
    }
    public void playAgainEntered(){
        playAgainButton.setTextFill(Color.BLACK);
        playAgainButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void playAgainExited(){
        playAgainButton.setTextFill(Color.CHARTREUSE);
        playAgainButton.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void exitEntered(){
        exitButton.setTextFill(Color.BLACK);
        exitButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void exitExited(){
        exitButton.setTextFill(Color.CHARTREUSE);
        exitButton.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void exitClicked(){
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitExited();
        playAgainExited();

    }
}
