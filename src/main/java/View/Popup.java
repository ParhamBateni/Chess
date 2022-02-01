package View;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Popup {
    public static Stage popupStage;
    static{
        popupStage=new Stage();
        popupStage.setResizable(false);
        popupStage.initStyle(StageStyle.UNDECORATED);
//        popupStage.setAlwaysOnTop(true);
        popupStage.initOwner(GameMenu.gameMenuStage);
    }

    public static void showEndGame(String message){
        try {
            URL fxmlAddress = Popup.class.getResource("/Visuals/fxml/EndGamePopupMenu.fxml");
            Pane pane = FXMLLoader.load(fxmlAddress);
            Label label=(Label) ((Pane)pane.getChildren().get(0)).getChildren().get(0);
            label.setText(message);
            Scene scene = new Scene(pane);
            popupStage.setScene(scene);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    System.exit(0);
                }
            });
            popupStage.show();
        } catch (IOException e) {

        }
    }
    public static ArrayList<StackPane> getPromotionStackPanes(){
        ArrayList<StackPane>promotionStackPanes=new ArrayList<>();
        try{
            URL fxmlAddress=Popup.class.getResource("/Visuals/fxml/promotion.fxml");
            Pane pane=FXMLLoader.load(fxmlAddress);
            for(Node node:pane.getChildren()){
                if(node instanceof StackPane) promotionStackPanes.add((StackPane) node);
            }
            Scene scene=new Scene(pane);
            popupStage.setScene(scene);
        } catch (IOException e) {
        }
        return promotionStackPanes;
    }
}
