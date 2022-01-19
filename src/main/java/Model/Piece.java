package Model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;

public class Piece {
    public enum Color{
        WHITE("white"),
        BLACK("black");

        public String name;
        Color(String name){
            this.name=name;
        }
    };
    public enum Type{
        ROOK("rook"),
        KNIGHT("knight"),
        BISHOP("bishop"),
        QUEEN("queen"),
        KING("king"),
        PAWN("pawn");

        public String name;
        Type(String name){
            this.name=name;
        }
    };
    public Type type;
    public Color color;
    public int[] coordination;
    public int point;
    public ImageView image;

    public Piece(Type type,Color color){
        this.type=type;
        this.color=color;
        try {
            FileInputStream input = new FileInputStream("src\\resources\\Visuals\\png\\"+color.name+
                    "\\"+type.name+".png");
            this.image = new ImageView(new Image(input));
            image.setFitWidth(50);
            image.setFitHeight(50);
        }catch (Exception e){
            System.out.println("Can\'t find the resources");
        }
    }
}
