package Model;

import java.util.ArrayList;

public class Board {
    private ArrayList<Cell> cells;
    public Board(ArrayList<Cell>cells){
        this.cells=cells;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
