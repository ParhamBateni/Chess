package Controller;

import Model.Board;
import Model.Cell;
import Model.Game;
import Model.Piece;
import View.Popup;
import View.Sound;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {
    public GridPane boardGridPane;
    public Pane boardPane;
    private GameController gameController;

    public Button resignButton;
    public Button drawButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Node> nodes = boardGridPane.getChildren();
        ArrayList<Cell> cells = new ArrayList<>();
        int i=0;
        int j=0;
        for (Node node : nodes) {
            if (node instanceof StackPane) {
                cells.add(new Cell((StackPane) node, new int[]{i++, j}));
            }
            if(i==8){
                i=0;
                j+=1;
            }
        }
        Sound.play(Sound.SoundType.START);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        this.gameController = new GameController(this,new Game(new Board(cells)),boardPane);

    }

    public void resignEntered(MouseEvent mouseEvent) {
        resignButton.setStyle("-fx-font-weight:bold");
        resignButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY, Insets.EMPTY)));
        resignButton.setTextFill(Color.BLACK);
    }

    public void resignExited(MouseEvent mouseEvent) {
        resignButton.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY)));
        resignButton.setTextFill(Color.CHARTREUSE);
    }

    public void resignClicked(MouseEvent mouseEvent) {
        boardPane.getParent().setDisable(true);
        ArrayList<Button> confirmButtons=Popup.getResignConfirmButtons();
        Button yesButton=confirmButtons.get(0);
        Button noButton=confirmButtons.get(1);

        yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Popup.popupStage.close();
                gameController.resign();
            }
        });
        noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Popup.popupStage.close();
            }
        });
        Popup.popupStage.showAndWait();
        boardPane.getParent().setDisable(false);
    }

    public void drawEntered(MouseEvent mouseEvent) {
        drawButton.setStyle("-fx-font-weight: bold");
        drawButton.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY, Insets.EMPTY)));
        drawButton.setTextFill(Color.BLACK);
    }

    public void drawExited(MouseEvent mouseEvent) {
        drawButton.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
        drawButton.setTextFill(Color.CHARTREUSE);
    }

    public void drawClicked(MouseEvent mouseEvent) {
        //draw status true
        if(gameController.game.getCurrentTurnColor()== Piece.Color.WHITE){
            gameController.player1DrawOfferCounts-=1;
        }
        else{
            gameController.player2DrawOfferCounts-=1;
        }
        gameController.drawOfferRaised=true;
        drawButton.setDisable(true);
    }
}
