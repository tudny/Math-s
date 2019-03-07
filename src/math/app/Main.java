package math.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import math.app.controllers.MainController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/MainWindow.fxml"));
        AnchorPane root = loader.load();

        primaryStage.setTitle("Math's");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        MainController cont = loader.getController();
        cont.setAll(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
