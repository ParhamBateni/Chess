package Controller;

import Model.Board;
import Model.Cell;
import Model.Game;
import View.Sound;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {
    public GridPane boardGridPane;
    public Pane boardPane;
    private GameController gameController;

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.gameController = new GameController(new Game(new Board(cells)),boardPane);

    }
}
