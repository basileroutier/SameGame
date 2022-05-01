/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewFX;

import controller.Controller;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;
import model.Ball;
import model.ColorBall;
import model.Game;
import model.Point;
import model.State;

/**
 *
 * @author basile
 */
public class OtherView implements PropertyChangeListener {

    private PropertyChangeSupport pcs;
    
    private Controller controller;
    private Game game;
    private GameSetting settingGame;
    private Board boardFX;
    private ColorBall color;
    private boolean[][] verifBall;
    
    private GridPane boardPane;
    private StackPane root;
    
    

    /**
     * Basic controller that initialize the menu for the setting and launch a
     * game And other attribut in the class
     *
     * @param controller Controller: the controller to call the method
     * @param model Model: the Observer class
     */
    public OtherView(Controller controller, Game game) {
        pcs = new PropertyChangeSupport(this);
        this.controller = controller;
        this.game = game;
        
        settingGame = new GameSetting();
        boardFX = new Board();
        
        this.game.addPropertyChangeListener(this);
    }

    /**
     * Initialize all the JavaFx view of the game with all the component Add
     * inside the action of the button
     *
     * @param primaryStage Stage: the stage that contain all the component to
     * show it
     * @param infoBoard
     */
    public void start(Stage primaryStage, List<Object> infoBoard) {
        primaryStage.setTitle("SameGame Copy");
        
        root = new StackPane();
        
        root.setMinSize(100, 100);
        root.setMaxSize(1000, 750);
        
        modifyScene(infoBoard);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void modifyScene(List<Object> infoBoard){
        int[] widthHeight = controller.getWidthHeight();
        verifBall = new boolean[widthHeight[0]][widthHeight[1]];
        Ball[][] board = (Ball[][]) infoBoard.get(0);
        
        var boardPane = boardFX.getMenuStart(board);
        boardPane.setBackground(new Background(new BackgroundFill(Color.rgb(145, 149, 156, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        
        root.getChildren().add(boardPane);
        
        root.setAlignment(Pos.CENTER);
        StackPane.setAlignment(boardPane, Pos.BOTTOM_CENTER);
        StackPane.setMargin(boardPane, new Insets(0,0,20,0));
    }
    
    private boolean checkIfPositionIsAlreadyVerif(Point position){
        for(var i=0;i<verifBall.length;i++){
            for(var j=0;j<verifBall[0].length;j++){
                if(position.getX()==i && position.getY()==j && verifBall[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get an event from the Observer class that he watch Check which button or
     * action has been give And ask to the controller or display something
     * depending the event
     *
     * @param evt PropertyChangeEvent: the notification that append if something
     * have change
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventChange = evt.getPropertyName();
        List<Object> infoBoard;
        switch (eventChange) {
            case Game.PROPERTY_START:
                break;
            case Game.PROPERTY_DELETE_BALL:
            case Game.PROPERTY_DELETE_UNDO:
                infoBoard = (List<Object>) evt.getNewValue();
                modifyScene(infoBoard);
                break;
            case Board.PROPERTY_CHECK_POSITION:
            case Board.PROPERTY_HOVER_CHECK_POSITION:
            case Game.PROPERTY_GET_VERIF_BALL:
            case GameFX.PROPERTY_ACTION_UNDO:
            case GameFX.PROPERTY_ACTION_REDO:
            case GameFX.PROPERTY_ACTION_RESTART:
            case GameFX.PROPERTY_ACTION_STOP:
            case Game.PROPERTY_GAME_ERROR:
            case Game.PROPERTY_GAME_WIN:
            case Game.PROPERTY_GAME_LOOSE:
                break;
            default:
                throw new AssertionError();
        }
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
    public void change(String buttonClick, String value) {
        pcs.firePropertyChange(buttonClick, null, value);
    }
}
