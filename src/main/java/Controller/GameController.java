package Controller;

import Model.Board;
import Model.Cell;
import Model.Game;
import Model.Piece;
import View.Popup;
import View.Sound;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

import static Model.Piece.Type.*;

public class GameController {
    private boolean whiteKingHasMoved = false;
    private boolean whiteRook1HasMoved = false;
    private boolean whiteRook2HasMoved = false;
    private boolean blackKingHasMoved = false;
    private boolean blackRook1HasMoved = false;
    private boolean blackRook2HasMoved = false;

    private Game game;
    private Pane boardPane;
    private HashMap<Piece, ArrayList<Cell>> cellChoices = new HashMap<>();
    private ArrayList<Cell> selectedCellChoices = new ArrayList<>();
    private boolean checkHappened = false;
    private boolean checkMateHappened = false;


    public GameController(Game game, Pane boardPane) {
        this.game = game;
        this.boardPane = boardPane;
        initGame();
    }

    private void initGame() {
        ArrayList<Cell> cells = game.board.getCells();
        //black init
        {
            Piece.Type[] types = {Piece.Type.ROOK, KNIGHT, Piece.Type.BISHOP, Piece.Type.QUEEN,
                    Piece.Type.KING, Piece.Type.BISHOP, KNIGHT, Piece.Type.ROOK};
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
            Piece.Type[] types = {Piece.Type.ROOK, KNIGHT, Piece.Type.BISHOP, Piece.Type.KING,
                    Piece.Type.QUEEN, Piece.Type.BISHOP, KNIGHT, Piece.Type.ROOK};
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
        findChoices();
        if (checkMateHappened) {
            endGame();
        }
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
        selectedCellChoices = cellChoices.get(Cell.selectedCell.piece);
        markChoices();
    }

    private void deselect() {
        for (int i = 0; i < selectedCellChoices.size(); i++) {
            Cell cell = selectedCellChoices.get(i);
            cell.rectangle.setOpacity(0);
            cell.removeMoveEventHandler();
        }
        Cell.deselectCell();
        selectedCellChoices = new ArrayList<>();
    }

    private void markChoices() {
        for (Cell cell : selectedCellChoices) {
            Rectangle rectangleChoice = cell.rectangle;
            rectangleChoice.setOpacity(0.5);
            if (cell.hasPiece()) {
                rectangleChoice.setFill(Color.RED);
            } else {
                rectangleChoice.setFill(Color.YELLOW);
            }
        }
        addMoveHandler();
    }

    private void findChoices() {
        for (Cell cell : game.board.getCells()) {
            if (cell.hasPiece() && cell.piece.color == game.getCurrentTurnColor()) {
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
                            int[] pawnHitChoice1 = new int[]{coordinates[0] + 1, coordinates[1] + direction};
                            int[] pawnHitChoice2 = new int[]{coordinates[0] - 1, coordinates[1] + direction};
                            try {
                                Cell hitChoice1 = game.board.findCell(pawnHitChoice1);
                                if (hitChoice1.hasPiece() && hitChoice1.piece.color == game.getOpponentTurnColor()) {
                                    choices.add(pawnHitChoice1);
                                }
                            } catch (Exception e) {
                            }
                            try {
                                Cell hitChoice2 = game.board.findCell(pawnHitChoice2);
                                if (hitChoice2.hasPiece() && hitChoice2.piece.color == game.getOpponentTurnColor()) {
                                    choices.add(pawnHitChoice2);
                                }
                            } catch (Exception e) {
                            }
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
                        int i = coordinates[0];
                        int j = coordinates[1];
                        ArrayList<int[]> kingChoices = new ArrayList<>();

                        kingChoices.add(new int[]{i + 1, j});
                        kingChoices.add(new int[]{i, j + 1});
                        kingChoices.add(new int[]{i - 1, j});
                        kingChoices.add(new int[]{i, j - 1});
                        kingChoices.add(new int[]{i + 1, j + 1});
                        kingChoices.add(new int[]{i + 1, j - 1});
                        kingChoices.add(new int[]{i - 1, j + 1});
                        kingChoices.add(new int[]{i - 1, j - 1});


                        //check for castling
                        checkCastle(cell);

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
                ArrayList<Cell> pieceChoices = new ArrayList<>();
                for (int[] choice : choices) {
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (checkHappens(cell, cellChoice)) {
                            continue;
                        }
                        pieceChoices.add(cellChoice);

                    } catch (Exception e) {
                        continue;
                    }
                }
                if(cellChoices.containsKey(cell.piece)){
                    cellChoices.get(cell.piece).addAll(pieceChoices);
                }else cellChoices.put(cell.piece, pieceChoices);
            }
        }
        //check if check mate happened or not!
        for (ArrayList<Cell> choices : cellChoices.values()) {
            if (choices.size() != 0) {
                return;
            }
        }
        checkMateHappened = true;
    }

    private void checkCastle(Cell kingCell) {
        int[] coordinates = kingCell.coordinates;
        if (game.getCurrentTurnColor() == Piece.Color.WHITE) {
            if (whiteKingHasMoved || (whiteRook1HasMoved && whiteRook2HasMoved) || checkHappened) {
                return;
            } else {
                try {
                    if (!whiteRook1HasMoved) {
                        Cell cell1 = game.board.findCell(coordinates[0]+1, coordinates[1]);
                        Cell cell2 = game.board.findCell(coordinates[0]+2, coordinates[1]);
                        Cell rook1Cell = game.board.findCell(coordinates[0]+3, coordinates[1]);
                        if (!cell1.hasPiece() && !cell2.hasPiece()) {
                            if (!checkHappens(kingCell, cell1) && !checkHappens(kingCell, cell2)) {
                                ArrayList<Cell >choiceCell=new ArrayList<>();
                                choiceCell.add(cell2);
                                cellChoices.put(kingCell.piece,choiceCell);
                                cell2.setMoveEventHandler(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        Sound.play(Sound.SoundType.CASTLE);

                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                        }
                                        whiteRook1HasMoved=true;
                                        whiteKingHasMoved=true;

                                        cell2.setPiece(kingCell.piece);
                                        kingCell.removePiece();
                                        kingCell.removeSelectEventHandler();
                                        cell1.setPiece(rook1Cell.piece);
                                        rook1Cell.removePiece();
                                        handleCheck(cell1);
                                        deselect();
                                        changeTurn();
                                    }
                                });
                            }
                        }
                    }
                    if (!whiteRook2HasMoved) {
                        Cell cell1 = game.board.findCell(coordinates[0]-1, coordinates[1] );
                        Cell cell2 = game.board.findCell(coordinates[0]-2, coordinates[1]);
                        Cell cell3 = game.board.findCell(coordinates[0]-3, coordinates[1]);
                        Cell rook2Cell = game.board.findCell(coordinates[0]-4, coordinates[1] );
                        if (!cell1.hasPiece() && !cell2.hasPiece() && !cell3.hasPiece()) {
                            if (!checkHappens(kingCell, cell1) && !checkHappens(kingCell, cell2)) {
                                ArrayList<Cell >choiceCell=new ArrayList<>();
                                choiceCell.add(cell2);
                                cellChoices.put(kingCell.piece,choiceCell);
                                cell2.setMoveEventHandler(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        Sound.play(Sound.SoundType.CASTLE);
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                        }
                                        whiteRook2HasMoved=true;
                                        whiteKingHasMoved=true;

                                        cell2.setPiece(kingCell.piece);
                                        kingCell.removePiece();
                                        kingCell.removeSelectEventHandler();
                                        cell1.setPiece(rook2Cell.piece);
                                        rook2Cell.removePiece();
                                        handleCheck(cell1);
                                        deselect();
                                        changeTurn();
                                    }
                                });
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
        } else {
            if (blackKingHasMoved || (blackRook1HasMoved && blackRook2HasMoved) || checkHappened) {
                return;
            } else {
                try {
                    if (!blackRook1HasMoved) {
                        Cell cell1 = game.board.findCell(coordinates[0]+1, coordinates[1]);
                        Cell cell2 = game.board.findCell(coordinates[0]+2, coordinates[1]);
                        Cell rook1Cell = game.board.findCell(coordinates[0]+3, coordinates[1]);
                        if (!cell1.hasPiece() && !cell2.hasPiece()) {
                            if (!checkHappens(kingCell, cell1) && !checkHappens(kingCell, cell2)) {
                                ArrayList<Cell >choiceCell=new ArrayList<>();
                                choiceCell.add(cell2);
                                cellChoices.put(kingCell.piece,choiceCell);
                                cell2.setMoveEventHandler(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        Sound.play(Sound.SoundType.CASTLE);
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                        }
                                        blackRook1HasMoved=true;
                                        blackKingHasMoved=true;

                                        cell2.setPiece(kingCell.piece);
                                        kingCell.removePiece();
                                        kingCell.removeSelectEventHandler();
                                        cell1.setPiece(rook1Cell.piece);
                                        rook1Cell.removePiece();
                                        handleCheck(cell1);
                                        deselect();
                                        changeTurn();
                                    }
                                });
                            }
                        }
                    }
                    if (!blackRook2HasMoved) {
                        Cell cell1 = game.board.findCell(coordinates[0]-1, coordinates[1] );
                        Cell cell2 = game.board.findCell(coordinates[0]-2, coordinates[1]);
                        Cell cell3 = game.board.findCell(coordinates[0]-3, coordinates[1]);
                        Cell rook2Cell = game.board.findCell(coordinates[0]-4, coordinates[1] );

                        if (!cell1.hasPiece() && !cell2.hasPiece() && !cell3.hasPiece()) {
                            if (!checkHappens(kingCell, cell1) && !checkHappens(kingCell, cell2)) {
                                ArrayList<Cell >choiceCell=new ArrayList<>();
                                choiceCell.add(cell2);
                                cellChoices.put(kingCell.piece,choiceCell);
                                cell2.setMoveEventHandler(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        Sound.play(Sound.SoundType.CASTLE);
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                        }
                                        blackRook2HasMoved=true;
                                        blackKingHasMoved=true;

                                        cell2.setPiece(kingCell.piece);
                                        kingCell.removePiece();
                                        kingCell.removeSelectEventHandler();
                                        cell1.setPiece(rook2Cell.piece);
                                        rook2Cell.removePiece();
                                        handleCheck(cell1);
                                        deselect();
                                        changeTurn();
                                    }
                                });
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
        }
    }

    private void endGame() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Sound.play(Sound.SoundType.END);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            Platform.runLater(() -> Popup.showEndGame(String.format("%s won the game by checkMate!",
                    game.getOpponentTurnColor())));
        }).start();
    }

    private boolean checkHappens(Cell cellSelected, Cell cellChoice) {
        boolean checkHappens = false;
        cellChoice.checkSetPiece(cellSelected.piece);
        cellSelected.checkRemovePiece();
        for (Cell cell : game.board.getCells()) {
            if (cell.hasPiece() && cell.piece.color == game.getOpponentTurnColor()) {
                if (threatensKing(cell, game.getCurrentTurnColor())) {
                    checkHappens = true;
                    break;
                }
            }
        }
        cellSelected.checkSetPiece(cellChoice.piece);
        cellChoice.checkRemovePiece();
        return checkHappens;
    }

    private void handleCheck(Cell cellChoice) {
        ArrayList<Piece.Type> piecesToBeChecked = new ArrayList<>();
        piecesToBeChecked.add(ROOK);
        piecesToBeChecked.add(QUEEN);
        piecesToBeChecked.add(BISHOP);
        for (Cell cell : game.board.getCells()) {
            if (!cell.hasPiece()) continue;
            if (piecesToBeChecked.contains(cell.piece.type) && cell.piece.color == game.getCurrentTurnColor() || cell == cellChoice) {
                if (threatensKing(cell, game.getOpponentTurnColor())) {
                    checkHappened = true;
                    Sound.play(Sound.SoundType.CHECK);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    return;
                }
            }
        }
        checkHappened = false;
    }

    private boolean threatensKing(Cell cell, Piece.Color kingColor) {
        int[] coordinates = cell.coordinates;
        switch (cell.piece.type) {
            case PAWN: {
                int direction = -1;
                if (cell.piece.color == Piece.Color.BLACK) direction *= -1;
                try {
                    Cell choice = game.board.findCell(new int[]{coordinates[0] + 1, coordinates[1] + direction});
                    if (choice.piece.type == KING && choice.piece.color == kingColor) {
                        return true;
                    }
                } catch (Exception e) {
                }
                try {
                    Cell choice = game.board.findCell(new int[]{coordinates[0] - 1, coordinates[1] + direction});
                    if (choice.piece.type == KING && choice.piece.color == kingColor) {
                        return true;
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
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) return true;
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
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 0) {
                    int[] choice = new int[]{++i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 0 && j != 7) {
                    int[] choice = new int[]{--i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 7) {
                    int[] choice = new int[]{++i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
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
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (i != 8) {
                    int[] choice = new int[]{++i, j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (j != -1) {
                    int[] choice = new int[]{i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }

                }
                j = coordinates[1];
                while (j != 8) {
                    int[] choice = new int[]{i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
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
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 0) {
                    int[] choice = new int[]{++i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 0 && j != 7) {
                    int[] choice = new int[]{--i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                j = coordinates[1];
                while (i != 7 && j != 7) {
                    int[] choice = new int[]{++i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }

                i = coordinates[0];
                j = coordinates[1];
                while (i != -1) {
                    int[] choice = new int[]{--i, j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (i != 8) {
                    int[] choice = new int[]{++i, j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                i = coordinates[0];
                while (j != -1) {
                    int[] choice = new int[]{i, --j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }

                }
                j = coordinates[1];
                while (j != 8) {
                    int[] choice = new int[]{i, ++j};
                    try {
                        Cell cellChoice = game.board.findCell(choice);
                        if (cellChoice.piece.type == KING && cellChoice.piece.color == kingColor) {
                            return true;
                        }
                        break;
                    } catch (Exception e) {
                    }
                }
                break;
            }
            case KING: {
                int i = coordinates[0];
                int j = coordinates[1];
                ArrayList<int[]> kingChoices = new ArrayList<>();
                kingChoices.add(new int[]{i + 1, j});
                kingChoices.add(new int[]{i, j + 1});
                kingChoices.add(new int[]{i - 1, j});
                kingChoices.add(new int[]{i, j - 1});
                kingChoices.add(new int[]{i + 1, j + 1});
                kingChoices.add(new int[]{i + 1, j - 1});
                kingChoices.add(new int[]{i - 1, j + 1});
                kingChoices.add(new int[]{i - 1, j - 1});

                for (int[] choice : kingChoices) {
                    try {
                        Cell choiceCell = game.board.findCell(choice);
                        if (choiceCell.piece.type == KING && choiceCell.piece.color == kingColor) {
                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
                break;
            }
        }
        return false;
    }

    private void addMoveHandler() {
        for (Cell cellChoice : cellChoices.get(Cell.selectedCell.getPiece())) {
            EventHandler moveHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (cellChoice.hasPiece()) {
                        Sound.play(Sound.SoundType.CAPTURE);
                    } else Sound.play(Sound.SoundType.MOVE);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }

                    //update king and rook conditions for castling
                    if(Cell.selectedCell.piece.type==KING){
                        if(Cell.selectedCell.piece.color== Piece.Color.WHITE){
                            whiteKingHasMoved=true;
                        }
                        else{
                            blackKingHasMoved=true;
                        }
                    }
                    else if(Cell.selectedCell.piece.type==ROOK){
                        if(Cell.selectedCell.piece.color==Piece.Color.WHITE) {
                            if (Cell.selectedCell.coordinates[0] > 4) whiteRook1HasMoved=true;
                            else whiteRook2HasMoved=true;
                        }
                        else{
                            if(Cell.selectedCell.coordinates[0]>4)blackRook1HasMoved=true;
                            else blackRook2HasMoved=true;
                        }
                    }


                    if (cellChoice.hasPiece()) cellChoice.removePiece();
                    cellChoice.setPiece(Cell.selectedCell.piece);
                    Cell.selectedCell.removePiece();
                    Cell.selectedCell.removeSelectEventHandler();
                    handlePromotion(cellChoice);
                    handleCheck(cellChoice);
                    deselect();
                    changeTurn();
                }

            };
            if(!cellChoice.hasMoveEventHandler()){
                cellChoice.setMoveEventHandler(moveHandler);
            }
        }
    }
    private void handlePromotion(Cell cell){
        Piece piece=cell.piece;
        if(piece.type==PAWN && (cell.coordinates[1]==0 || cell.coordinates[1]==7)){
            deselect();
            deactivate(game.getCurrentTurnColor());
            ArrayList<StackPane>promotionStackPanes=Popup.getPromotionStackPanes();
            StackPane queenStackPane=promotionStackPanes.get(0);
            StackPane knightStackPane=promotionStackPanes.get(1);
            StackPane rookStackPane=promotionStackPanes.get(2);
            StackPane bishopStackPane=promotionStackPanes.get(3);

            Piece.Color color=game.getCurrentTurnColor();
            Piece queen=new Piece(QUEEN,color);
            Piece knight=new Piece(KNIGHT,color);
            Piece rook=new Piece(ROOK,color);
            Piece bishop=new Piece(BISHOP,color);

            queenStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));
            knightStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));
            rookStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));
            bishopStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY, Insets.EMPTY)));

            queenStackPane.getChildren().add(queen.image);
            knightStackPane.getChildren().add(knight.image);
            rookStackPane.getChildren().add(rook.image);
            bishopStackPane.getChildren().add(bishop.image);

            queenStackPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    queenStackPane.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });
            queenStackPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    queenStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });

            knightStackPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    knightStackPane.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });
            knightStackPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    knightStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });

            rookStackPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rookStackPane.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });
            rookStackPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rookStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });

            bishopStackPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    bishopStackPane.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });
            bishopStackPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    bishopStackPane.setBackground(new Background(new BackgroundFill(Color.GRAY,CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }
            });


            queenStackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cell.removePiece();
                    cell.setPiece(queen);
                    cell.piece.image.setRotate(boardPane.getRotate());
                    Popup.popupStage.close();
                }
            });

            knightStackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cell.removePiece();
                    cell.setPiece(knight);
                    cell.piece.image.setRotate(boardPane.getRotate());
                    Popup.popupStage.close();
                }
            });

            rookStackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cell.removePiece();
                    cell.setPiece(rook);
                    cell.piece.image.setRotate(boardPane.getRotate());
                    Popup.popupStage.close();
                }
            });

            bishopStackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cell.removePiece();
                    cell.setPiece(bishop);
                    cell.piece.image.setRotate(boardPane.getRotate());
                    Popup.popupStage.close();
                }
            });
            Popup.popupStage.showAndWait();
        }
    }


    private void changeTurn() {
        flipBoard();
        cellChoices = new HashMap<>();
        Piece.Color opponentColor = game.getOpponentTurnColor();
        game.setOpponentTurnColor(game.getCurrentTurnColor());
        game.setCurrentTurnColor(opponentColor);
        deactivate(game.getOpponentTurnColor());
        activate(game.getCurrentTurnColor());
    }

    private void flipBoard() {
        RotateTransition rotate = new RotateTransition();
        rotate.setByAngle(180);
        rotate.setDuration(Duration.millis(1000));
        rotate.setAxis(new Point3D(20, 0, 0));
        rotate.setNode(boardPane);
        rotate.play();
        for (Cell cell : game.board.getCells()) {
            if (cell.hasPiece()) {
                RotateTransition imageRotate = new RotateTransition();
                imageRotate.setNode(cell.piece.image);
                imageRotate.setByAngle(180);
                imageRotate.setDelay(Duration.millis(500));
                imageRotate.setAxis(new Point3D(20, 0, 0));
                imageRotate.play();
            }
        }
    }
}
