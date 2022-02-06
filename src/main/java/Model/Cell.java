package Model;

import View.GameMenu;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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
    public EventHandler<MouseEvent>enterSelectEventHandler;
    public EventHandler<MouseEvent>exitSelectEventHandler;
    public EventHandler<MouseEvent>enterMoveEventHandler;
    public EventHandler<MouseEvent>exitMoveEventHandler;
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
        try {
            stackPane.getChildren().add(piece.image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkSetPiece(Piece piece){
        this.previousPiece=this.piece;
        this.piece=piece;
    }
    public void checkRemovePiece(){
        this.piece=previousPiece;
        this.previousPiece=null;
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
        if(this.selectEventHandler==null) {
            stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, selectEventHandler);
            enterSelectEventHandler=new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    GameMenu.gameMenuStage.getScene().setCursor(Cursor.OPEN_HAND);
                }
            };
            stackPane.addEventHandler(MouseEvent.MOUSE_ENTERED,enterSelectEventHandler);
            exitSelectEventHandler=new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    GameMenu.gameMenuStage.getScene().setCursor(Cursor.DEFAULT);
                }
            };
            stackPane.addEventHandler(MouseEvent.MOUSE_EXITED,exitSelectEventHandler);
            this.selectEventHandler = selectEventHandler;
        }
    }

    public void setMoveEventHandler(EventHandler<MouseEvent> moveEventHandler) {
        if(this.moveEventHandler!=null){
            stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED,this.moveEventHandler);
        }
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED,moveEventHandler);
        enterMoveEventHandler=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                GameMenu.gameMenuStage.getScene().setCursor(Cursor.CLOSED_HAND);
            }
        };
        stackPane.addEventHandler(MouseEvent.MOUSE_ENTERED,enterMoveEventHandler);
        exitMoveEventHandler=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                GameMenu.gameMenuStage.getScene().setCursor(Cursor.DEFAULT);
            }
        };
        stackPane.addEventHandler(MouseEvent.MOUSE_EXITED,exitMoveEventHandler);
        this.moveEventHandler=moveEventHandler;
    }
    public void removeSelectEventHandler(){
        if(selectEventHandler!=null) {
            stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, selectEventHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_ENTERED,enterSelectEventHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_EXITED,exitSelectEventHandler);
            this.selectEventHandler = null;
        }
    }

    public void removeMoveEventHandler(){
        if(moveEventHandler!=null) {
            stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, moveEventHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_ENTERED,enterMoveEventHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_EXITED,exitMoveEventHandler);
            this.moveEventHandler = null;
        }
    }
    public boolean hasMoveEventHandler(){
        return this.moveEventHandler!=null;
    }

}
