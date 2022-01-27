package Controller;

import Model.Board;
import Model.Cell;
import Model.Game;
import Model.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GameController {
    private Game game;

    private ArrayList<Cell> cellChoices = new ArrayList<>();

    public GameController(Game game) {
        this.game = game;
        initGame();
    }

    private void initGame() {
        ArrayList<Cell> cells = game.board.getCells();
        //black init
        {
            Piece.Type[] types = {Piece.Type.ROOK, Piece.Type.KNIGHT, Piece.Type.BISHOP, Piece.Type.QUEEN,
                    Piece.Type.KING, Piece.Type.BISHOP, Piece.Type.KNIGHT, Piece.Type.ROOK};
            int index = 0;
            for (Piece.Type type : types) {
                cells.get(index++).setPiece(new Piece(type, Piece.Color.BLACK));
            }
            for (int i = index; i < 16; i++) {
                cells.get(i).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.BLACK));
            }
        }

        //white init
        {
            Piece.Type[] types = {Piece.Type.ROOK, Piece.Type.KNIGHT, Piece.Type.BISHOP, Piece.Type.KING,
                    Piece.Type.QUEEN, Piece.Type.BISHOP, Piece.Type.KNIGHT, Piece.Type.ROOK};
            int index = 63;
            for (Piece.Type type : types) {
                cells.get(index--).setPiece(new Piece(type, Piece.Color.WHITE));
            }
            for (int i = index; i >= 48; i--) {
                cells.get(i).setPiece(new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
            }
        }
        activate(game.getCurrentTurnColor());
    }

    private void activate(Piece.Color color) {
        for (Cell cell : game.board.getCells()) {
            if (cell.hasPiece() && cell.piece.color == color) {
                cell.setSelectEventHandler(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            Board board = game.board;
                            int[] coordinates = cell.coordinates;
                            Cell cell = board.findCell(coordinates);
                            if (Cell.selectedCell == cell) deselect();
                            else {
                                deselect();
                                select(cell);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void deactivate(Piece.Color color) {
        for (Cell cell : game.board.getCells()) {
            if (cell.hasPiece() && cell.piece.color == color) {
                cell.removeSelectEventHandler();
            }
        }
    }

    private void select(Cell cell) {
        cell.selectCell();
        Rectangle rectangle = cell.rectangle;
        rectangle.opacityProperty().set(0.5);
        rectangle.setFill(Color.CHARTREUSE);
        markChoices();
    }

    private void deselect() {
        for (int i = 0; i < cellChoices.size(); i++) {
            Cell cell = cellChoices.get(i);
            cell.rectangle.setOpacity(0);
            cell.removeMoveEventHandler();
        }
        Cell.deselectCell();
        cellChoices.clear();
    }

    private void markChoices() {
        Cell cell = Cell.selectedCell;
        int[] coordinates = cell.coordinates;
        Piece piece = cell.piece;
        int direction = -1;
        if (piece.color == Piece.Color.BLACK) direction *= -1;
        ArrayList<int[]> choices = new ArrayList<>();
        switch (piece.type) {
            case PAWN: {
                int[] pawnFirstChoice = new int[]{coordinates[0], coordinates[1] + direction};
                try {
                    Cell firstChoiceCell = game.board.findCell(pawnFirstChoice);
                    if (!firstChoiceCell.hasPiece()) {
                        choices.add(pawnFirstChoice);
                        if (cell.coordinates[1] == 3.5 - direction * 2.5) {
                            int[] pawnSecondChoice = new int[]{coordinates[0], coordinates[1] + 2 * direction};
                            Cell secondChoiceCell = game.board.findCell(pawnSecondChoice);
                            if (!secondChoiceCell.hasPiece()) {
                                choices.add(pawnSecondChoice);
                            }
                        }
                    }
                } catch (Exception e) {
                }
                break;
            }
            case KNIGHT: {
                ArrayList<int[]> knightChoices = new ArrayList<>();
                knightChoices.add(new int[]{coordinates[0] + 1, coordinates[1] + 2});
                knightChoices.add(new int[]{coordinates[0] - 1, coordinates[1] + 2});
                knightChoices.add(new int[]{coordinates[0] + 2, coordinates[1] + 1});
                knightChoices.add(new int[]{coordinates[0] - 2, coordinates[1] + 1});

                knightChoices.add(new int[]{coordinates[0] + 2, coordinates[1] - 1});
                knightChoices.add(new int[]{coordinates[0] - 2, coordinates[1] - 1});
                knightChoices.add(new int[]{coordinates[0] + 1, coordinates[1] - 2});
                knightChoices.add(new int[]{coordinates[0] - 1, coordinates[1] - 2});

                for (int[] choice : knightChoices) {
                    try {
                        Cell knightChoiceCell = game.board.findCell(choice);
                        if (!knightChoiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (knightChoiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                break;
            }
            case BISHOP: {
                int i = coordinates[0];
                int j = coordinates[1];
                while (i != 0 && j != 0) {
                    int[] choice = new int[]{--i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 0) {
                    int[] choice = new int[]{++i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 0 && j != 7) {
                    int[] choice = new int[]{--i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 7) {
                    int[] choice = new int[]{++i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                break;
            }
            case ROOK: {
                int i = coordinates[0];
                int j = coordinates[1];
                while (i != -1) {
                    int[] choice = new int[]{--i, j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (i != 8) {
                    int[] choice = new int[]{++i, j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (j != -1) {
                    int[] choice = new int[]{i, --j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }

                }
                j = coordinates[1];
                while (j != 8) {
                    int[] choice = new int[]{i, ++j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }

                }
                break;
            }
            case QUEEN: {
                int i = coordinates[0];
                int j = coordinates[1];
                while (i != 0 && j != 0) {
                    int[] choice = new int[]{--i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 0) {
                    int[] choice = new int[]{++i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 0 && j != 7) {
                    int[] choice = new int[]{--i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 7) {
                    int[] choice = new int[]{++i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (!cellChoice.hasPiece()) choices.add(choice);
                        else {
                            if (cellChoice.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }

                i = coordinates[0];
                j = coordinates[1];
                while (i != -1) {
                    int[] choice = new int[]{--i, j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (i != 8) {
                    int[] choice = new int[]{++i, j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (j != -1) {
                    int[] choice = new int[]{i, --j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }

                }
                j = coordinates[1];
                while (j != 8) {
                    int[] choice = new int[]{i, ++j};
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) choices.add(choice);
                        else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                            break;
                        }
                    } catch (Exception e) {
                    }

                }
                break;
            }
            case KING: {
                //todo check if the choice is a safe cell
                int i = coordinates[0];
                int j = coordinates[1];
                ArrayList<int[]> kingChoices = new ArrayList<>();
                kingChoices.add(new int[]{i + 1, j});
                kingChoices.add(new int[]{i, j + 1});
                kingChoices.add(new int[]{i - 1, j});
                kingChoices.add(new int[]{i, j - 1});
                for (int[] choice : kingChoices) {
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (!choiceCell.hasPiece()) {
                            choices.add(choice);
                        } else {
                            if (choiceCell.piece.color != game.getCurrentTurnColor()) {
                                choices.add(choice);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                break;
            }
        }

        for (int[] choice : choices) {
            try {
                Cell cellChoice = game.board.findCell(choice);
                Rectangle rectangleChoice = cellChoice.getRectangle();
                cellChoices.add(cellChoice);
                rectangleChoice.setOpacity(0.5);
                rectangleChoice.setFill(Color.YELLOW);
            } catch (Exception e) {
                continue;
            }
        }
        addMoveHandler();
    }

    private void addMoveHandler() {
        for (Cell cellChoice : cellChoices) {
            EventHandler moveHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cellChoice.setPiece(Cell.selectedCell.piece);
                    Cell.selectedCell.removePiece();
                    Cell.selectedCell.removeSelectEventHandler();
                    deselect();
                    changeTurn();
                }
            };
            cellChoice.setMoveEventHandler(moveHandler);
        }
    }

    private void changeTurn() {
        //todo write a function to flip the board
//        flipBoard();
        if (game.getCurrentTurnColor() == Piece.Color.BLACK) {
            game.setCurrentTurnColor(Piece.Color.WHITE);
            activate(Piece.Color.WHITE);
            deactivate(Piece.Color.BLACK);

        } else {
            game.setCurrentTurnColor(Piece.Color.BLACK);
            activate(Piece.Color.BLACK);
            deactivate(Piece.Color.WHITE);
        }
    }
}
