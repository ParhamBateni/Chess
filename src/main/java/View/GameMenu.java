package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GameMenu extends Application{


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlAddress = getClass().getResource("/Visuals/fxml/GameMenu.fxml");
        Parent pane = FXMLLoader.load(fxmlAddress);
        Scene scene = new Scene(pane);
        stage.setTitle("Chess :)");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
