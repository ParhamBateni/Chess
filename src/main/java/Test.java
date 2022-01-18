//import javafx.application.Application;
//        import javafx.beans.property.ObjectProperty;
//        import javafx.beans.property.SimpleObjectProperty;
//        import javafx.geometry.Point2D;
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//        import javafx.scene.Node;
//        import javafx.scene.Scene;
//        import javafx.scene.image.ImageView;
//        import javafx.scene.layout.Pane;
//        import javafx.scene.layout.StackPane;
//        import javafx.scene.paint.Color;
//
//
//import java.io.FileInputStream;
//
//
//public class Test extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            Pane root = new Pane();
//
//            StackPane imageContainer = new StackPane();
//            ImageView image = new ImageView(new Image(new FileInputStream("src\\resources\\Visuals\\png\\queen.png")));
//            imageContainer.getChildren().addAll(new StackPane(64, 48, Color.CORNFLOWERBLUE), image);
//            enableDragging(imageContainer);
//
//            root.getChildren().add(imageContainer);
//
//            Scene scene = new Scene(root,800,600);
//            try {
//                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//            }
//            catch (Exception e){}
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void enableDragging(Node node) {
//        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
//        node.setOnMousePressed( event -> mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY())));
//        node.setOnMouseDragged( event -> {
//            double deltaX = event.getSceneX() - mouseAnchor.get().getX();
//            double deltaY = event.getSceneY() - mouseAnchor.get().getY();
//            node.relocate(node.getLayoutX()+deltaX, node.getLayoutY()+deltaY);
//            mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY()));;
//        });
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
