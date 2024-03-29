package Model;

public class Game {
    public Board board;
    private Player player1;
    private Player player2;
    private Piece.Color currentTurnColor;
    private Piece.Color opponentTurnColor;

    public Game(Board board/*,Player player1,Player player2*/){
        this.board=board;
        board.setGame(this);
        currentTurnColor= Piece.Color.WHITE;
        opponentTurnColor=Piece.Color.BLACK;
//        this.player1=player1;
//        this.player2=player2;
    }

    public Piece.Color getCurrentTurnColor() {
        return currentTurnColor;
    }

    public Piece.Color getOpponentTurnColor() {
        return opponentTurnColor;
    }

    public void setCurrentTurnColor(Piece.Color currentTurnColor) {
        this.currentTurnColor = currentTurnColor;
    }

    public void setOpponentTurnColor(Piece.Color opponentTurnColor) {
        this.opponentTurnColor = opponentTurnColor;
    }
}
