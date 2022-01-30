package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public abstract class Popup {
    public static Stage popupStage;

    public static void showEndGame(String message){
        try {
            URL fxmlAddress = Popup.class.getResource("/Visuals/fxml/endGamePopupMenu.fxml");
            popupStage=new Stage();
            Pane pane = FXMLLoader.load(fxmlAddress);
            Label label=(Label) ((Pane)pane.getChildren().get(0)).getChildren().get(0);
            label.setText(message);
            Scene scene = new Scene(pane);
            popupStage.setResizable(false);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setAlwaysOnTop(true);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (IOException e) {

        }
    }
}
