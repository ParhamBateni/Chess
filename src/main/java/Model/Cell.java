package Model;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    public static Cell selectedCell;


    private Piece piece;
    private StackPane stackPane;
    private Rectangle rectangle;
    private int[] coordinates;
    private Board board;

    public Cell(StackPane stackPane, int[] coordinates) {
        this.stackPane = stackPane;
        this.rectangle = (Rectangle) stackPane.getChildren().get(0);
        this.coordinates = coordinates;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        stackPane.getChildren().add(piece.image);
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Cell cell = board.findCell(coordinates[0], coordinates[1]);
                    if(Cell.selectedCell==cell)Cell.deselectCell();
                    else {
                        Cell.deselectCell();
                        selectCell(cell);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public static void deselectCell() {
        if (Cell.selectedCell==null)return;
        Cell.selectedCell.rectangle.setOpacity(0);
        Cell.selectedCell = null;
    }

    public void selectCell(Cell cell) {
        Piece.Color selectedColor=board.getGame().getCurrentTurnColor();
        if (piece.color!=selectedColor){
            return;
        }
        rectangle.opacityProperty().set(0.5);
        rectangle.setFill(Color.CHARTREUSE);
        selectedCell = cell;
        //todo: color some cells depending on the piece in the selected cell
    }
}
