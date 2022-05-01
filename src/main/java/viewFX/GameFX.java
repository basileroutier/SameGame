/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewFX;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Ball;

/**
 *
 * @author basile
 */
public class GameFX implements PropertyChangeListener{
    
    public final static String PROPERTY_ACTION_DELETE = "delete";
    public final static String PROPERTY_ACTION_UNDO = "undo";
    public final static String PROPERTY_ACTION_REDO = "redo";
    public final static String PROPERTY_ACTION_STOP = "stop";
    public final static String PROPERTY_ACTION_RESTART = "restart";
    private PropertyChangeSupport pcs;
    HBox undoRedoBut;
    Button restart;
    Button stop;
    Button undo;
    Button redo;
    BorderPane center;
    StackPane boardGame;
    private boolean isFinish;

    public GameFX(View view) {
        pcs = new PropertyChangeSupport(this);
        view.addPropertyChangeListener(this);
        isFinish = false;
    }

    public BorderPane getGameSetting(List<Object> infoBoard, Board boardBall) {
        boardGame = new StackPane();
        Ball[][] board = (Ball[][]) infoBoard.get(0);
        Integer score = (Integer) infoBoard.get(1);
        Integer bestScore = (Integer) infoBoard.get(2);
        Integer numberRemainBall = (Integer) infoBoard.get(3);

        center = new BorderPane();
        center.setMaxWidth(300);
        center.setMaxHeight(400);
        center.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        center.setBorder(new Border(new BorderStroke(Color.rgb(84, 90, 102, 0.15), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        VBox settingPersoGameCenter = new VBox();
        
        undoRedoBut = new HBox();
        undo = new Button("Undo");
        redo = new Button("Redo");
        stop = new Button("Stop");
        restart = new Button("Restart");
        undoRedoBut.getChildren().addAll(undo, redo, stop, restart);
        undo.setOnAction(e->change(PROPERTY_ACTION_UNDO));
        redo.setOnAction(e->change(PROPERTY_ACTION_REDO));
        stop.setOnAction(e->displayGameOver("Fin"));
        restart.setOnAction(e->checkGameOverRestart());
        
        HBox.setMargin(undo, new Insets(3, 5, 0, 0));
        HBox.setMargin(redo, new Insets(3, 5, 0, 5));
        HBox.setMargin(stop, new Insets(3, 5, 0, 5));
        HBox.setMargin(restart, new Insets(3, 5, 0, 5));
        
        HBox scorePersoGameLabel = new HBox();
        Label lblScoreDisplay = new Label("-Score-");
        Label lblBallDisplay = new Label("-Nombre de tuiles-");
        Label lblBestScoreDisplay = new Label("-Meilleure Score-");
        scorePersoGameLabel.getChildren().addAll(lblScoreDisplay,lblBallDisplay,lblBestScoreDisplay);

        HBox.setMargin(lblScoreDisplay, new Insets(3, 3, 0, 0));
        HBox.setMargin(lblBallDisplay, new Insets(3, 3, 0, 0));
        HBox.setMargin(lblBestScoreDisplay, new Insets(3, 3, 0, 5));

        HBox scorePersoGame = new HBox();
        Label lblScore = new Label(score + "");
        lblScore.setFont(Font.font("System", FontWeight.BOLD, 15));
        
        Label lblBall = new Label(numberRemainBall + "");
        lblBall.setFont(Font.font("System", FontWeight.BOLD, 15));
        
        Label lblBestScore = new Label(bestScore + "");
        lblBestScore.setFont(Font.font("System", FontWeight.BOLD, 15));
        scorePersoGame.getChildren().addAll(lblScore, lblBall ,lblBestScore);

        int decalageScore = 37;
        int decalageBestScore = 37;
        int decalageBall = 75;

        for (var i = 1; i < score.toString().length(); i++) {
            decalageScore -= 7;
        }
        
        for (var i = 1; i < numberRemainBall.toString().length(); i++) {
            decalageBall -= 7;
        }
        
        for (var i = 1; i < bestScore.toString().length(); i++) {
            decalageBestScore -= 7;
        }

        HBox.setMargin(lblScore, new Insets(0, decalageScore, 0, 0));
        HBox.setMargin(lblBall, new Insets(0, decalageBall, 0, 10));
        HBox.setMargin(lblBestScore, new Insets(0, 37, 0, decalageBestScore));

        undoRedoBut.setAlignment(Pos.CENTER);
        scorePersoGameLabel.setAlignment(Pos.CENTER_RIGHT);
        scorePersoGame.setAlignment(Pos.CENTER_RIGHT);
        
        
        VBox.setMargin(scorePersoGameLabel, new Insets(0, 0, 0, 3));
        VBox.setMargin(scorePersoGame, new Insets(0, 3, 5, 3));
        settingPersoGameCenter.getChildren().addAll(undoRedoBut, scorePersoGameLabel, scorePersoGame);

        center.setTop(settingPersoGameCenter);
        var boardPane = boardBall.getMenuStart(board);
        boardPane.setBackground(new Background(new BackgroundFill(Color.rgb(145, 149, 156, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        
        boardGame.getChildren().add(boardPane);
        
        center.setCenter(boardGame);
        BorderPane.setMargin(boardPane, new Insets(0, 10, 10, 10));

        return center;
    }
    
    private void displayGameOver(String error){
        BorderPane gameOverBorder = new BorderPane();
        gameOverBorder.setBackground(new Background(new BackgroundFill(Color.rgb(145, 149, 156, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        Label gameOver = new Label(""+error);
        gameOver.setFont(Font.font("System", FontWeight.BOLD, 25));
        gameOverBorder.setCenter(gameOver);
        undo.setDisable(true);
        redo.setDisable(true);
        stop.setDisable(true);
        isFinish = true;
        boardGame.getChildren().add(gameOverBorder);
        change(PROPERTY_ACTION_STOP);
    }
    
    private void checkGameOverRestart(){
        if(isFinish){
            boardGame.getChildren().remove(1);
            undo.setDisable(false);
            redo.setDisable(false);
            stop.setDisable(false);
            isFinish = false;
        }
        change(PROPERTY_ACTION_RESTART);
    }

    /**
     * Add an observable in the PropertyChange to Observe the current class
     *
     * @param listener PropertyChangeListener: the Listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove an observable in the PropertyChange to Observe the current class
     *
     * @param listener PropertyChangeListener: the Listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Notify all the class that Observe the current class from a modification
     *
     * @param listener PropertyChangeListener: the Listener
     */
    public void change(String buttonClick) {
        pcs.firePropertyChange(buttonClick, null, true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventChange = evt.getPropertyName();
        switch (eventChange) {
            case View.PROPERTY_GAME_OVER:
                displayGameOver((String) evt.getNewValue());
            default:
                
        }
    }
    

}
