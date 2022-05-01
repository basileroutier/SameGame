package samegame;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Game;
import model.Game;
import viewFX.View;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var game = new Game();
        var controller = new Controller(game, stage);
    }

    public static void main(String[] args) {
        launch();
    }

}