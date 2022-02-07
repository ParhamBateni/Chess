package View;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public abstract class Popup {
    public static Stage popupStage;

    public static void showEndGame(String message){
        try {
            URL fxmlAddress = Popup.class.getResource("/graphics/fxml/EndGamePopupMenu.fxml");
            Pane pane = FXMLLoader.load(fxmlAddress);
            Label label=(Label) ((Pane)pane.getChildren().get(0)).getChildren().get(0);
            label.setText(message);

            popupStage=new Stage();
            popupStage.setResizable(false);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.initOwner(GameMenu.gameMenuStage);

            Scene scene = new Scene(pane);
            popupStage.setScene(scene);
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
            URL fxmlAddress=Popup.class.getResource("/graphics/fxml/promotion.fxml");
            Pane pane=FXMLLoader.load(fxmlAddress);
            for(Node node:pane.getChildren()){
                if(node instanceof StackPane) promotionStackPanes.add((StackPane) node);
            }
            Scene scene=new Scene(pane);

            popupStage=new Stage();
            popupStage.setResizable(false);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(GameMenu.gameMenuStage);
            popupStage.setScene(scene);
        } catch (IOException e) {
        }
        return promotionStackPanes;
    }
    public static ArrayList<Button> getResignConfirmButtons(){
        ArrayList<Button>resignButtons=new ArrayList<>();
        try{
            URL fxmlAddress=Popup.class.getResource("/graphics/fxml/ResignConfirm.fxml");
            Pane pane=FXMLLoader.load(fxmlAddress);
            for(Node node:pane.getChildren()){
                if(node instanceof Button) resignButtons.add((Button) node);
            }
            Scene scene=new Scene(pane);

            popupStage=new Stage();
            popupStage.setResizable(false);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(GameMenu.gameMenuStage);
            popupStage.setScene(scene);
        } catch (IOException e) {
        }
        return resignButtons;

    }
    public static ArrayList<Button> getDrawConfirmButtons(){
        ArrayList<Button>resignButtons=new ArrayList<>();
        try{
            URL fxmlAddress=Popup.class.getResource("/graphics/fxml/DrawConfirm.fxml");
            Pane pane=FXMLLoader.load(fxmlAddress);
            for(Node node:pane.getChildren()){
                if(node instanceof Button) resignButtons.add((Button) node);
            }
            Scene scene=new Scene(pane);

            popupStage=new Stage();
            popupStage.setResizable(false);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initOwner(GameMenu.gameMenuStage);
            popupStage.setScene(scene);
        } catch (IOException e) {
        }
        return resignButtons;
    }
}
