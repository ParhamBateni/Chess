package Controller;

import Model.Board;
import Model.Cell;
import Model.Game;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {
    public Pane gameBoardPane;
    private GameController gameController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Node> cellsStackPanes=gameBoardPane.getChildren();
        ArrayList<Cell>cells=new ArrayList<>();
        for(Node StackPane:cellsStackPanes){
            if(StackPane instanceof StackPane) {
                cells.add(new Cell((StackPane) StackPane));
            }
        }
        this.gameController=new GameController(new Game(new Board(cells)));
    }

    public void cellClicked(MouseEvent mouseEvent) {
        try{
            System.out.println(gameController.getClass());
        }
        catch (Exception e){
            System.out.println("error");
        }
    }
    public void clicked(MouseEvent mouseEvent){

    }

    public void test(MouseEvent mouseEvent) {
        StackPane StackPane= (StackPane) mouseEvent.getSource();
//        StackPane.setFill(new Image);
    }
}
