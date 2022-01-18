package Controller;

import Model.Cell;
import Model.Game;
import Model.Piece;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.ArrayList;

public class GameController {
    private Game game;
    public GameController(Game game){
        this.game=game;
        initGame();
    }
    private void initGame(){
        ArrayList<Cell>cells=game.board.getCells();
        //black init
        {
            Piece.Type[] types={Piece.Type.ROOK,Piece.Type.KNIGHT,Piece.Type.BISHOP,Piece.Type.QUEEN,
                    Piece.Type.KING,Piece.Type.BISHOP,Piece.Type.KNIGHT,Piece.Type.ROOK};
            int index=0;
            for(Piece.Type type : types){
                cells.get(index++).setPiece(new Piece(type, Piece.Color.BLACK));
            }
            for(int i=index;i<16;i++){
                cells.get(i).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK));
            }
        }

        //white init
        {
            Piece.Type[] types={Piece.Type.ROOK,Piece.Type.KNIGHT,Piece.Type.BISHOP,Piece.Type.KING,
                    Piece.Type.QUEEN, Piece.Type.BISHOP,Piece.Type.KNIGHT,Piece.Type.ROOK};
            int index=63;
            for(Piece.Type type : types){
                cells.get(index--).setPiece(new Piece(type, Piece.Color.WHITE));
            }
            for(int i=index;i>=48;i--){
                cells.get(i).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
            }
        }


    }
}
