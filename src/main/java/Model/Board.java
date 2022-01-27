package Model;

import java.util.ArrayList;

public class Board {
    private ArrayList<Cell> cells;
    private Game game;
    public Board(ArrayList<Cell>cells){
        this.cells=cells;
        for (Cell cell:cells){
            cell.setBoard(this);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
    public Cell findCell(int cor[]) throws Exception{
        for (Cell cell:cells){
            int[]coordinate=cell.getCoordinates();
            int x=coordinate[0];
            int y=coordinate[1];
            if (x==cor[0] && y==cor[1]){
                return cell;
            }
        }
        throw new Exception(String.format("cell not found with coordination {%d,%d}",cor[0],cor[1] ));
    }
}
