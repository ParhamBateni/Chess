package Model;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.Stack;

public class Cell {
    public static Cell selectedCell;


    public Piece piece;
    private Piece previousPiece;
    public StackPane stackPane;
    public Rectangle rectangle;
    public EventHandler<MouseEvent> selectEventHandler;
    public EventHandler<MouseEvent>moveEventHandler;
    public int[] coordinates;
    public Board board;

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
    }
    public void checkSetPiece(Piece piece){
        this.previousPiece=this.piece;
        this.piece=piece;
    }
    public void checkRemovePiece(){
        this.piece=previousPiece;
    }
    public void removePiece(){
        if(stackPane.getChildren().size()==2){
            stackPane.getChildren().remove(1);
        }
        this.piece=null;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public static void deselectCell() {
        if (Cell.selectedCell==null)return;
        Cell.selectedCell.rectangle.setOpacity(0);
        Cell.selectedCell = null;
    }

    public void selectCell() {
        selectedCell = this;
    }

    public Piece getPiece() {
        return piece;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean hasPiece(){
        return this.piece!=null;
    }

    public void setSelectEventHandler(EventHandler<MouseEvent> selectEventHandler) {
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, selectEventHandler);
        this.selectEventHandler= selectEventHandler;
    }

    public void setMoveEventHandler(EventHandler<MouseEvent> moveEventHandler) {
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED,moveEventHandler);
        this.moveEventHandler=moveEventHandler;
    }
    public void removeSelectEventHandler(){
        if(selectEventHandler!=null) {
            stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, selectEventHandler);
            this.selectEventHandler = null;
        }
    }

    public void removeMoveEventHandler(){
        stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED,moveEventHandler);
        this.moveEventHandler=null;
    }

}
