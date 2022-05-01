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
public class View implements PropertyChangeListener {

    private PropertyChangeSupport pcs;
    
    private Controller controller;
    private Game game;
    private GameSetting settingGame;
    private Board boardFX;
    private GameFX gameFX;
    private ColorBall color;
    private boolean[][] verifBall;
    
    private BorderPane boardPane;
    private StackPane root;
    private OtherView other;
    
    private Stage secondStage;
    
    private Stage primaryStage;
    
    public final static String PROPERTY_GAME_OVER = "View.GameOver";

    /**
     * Basic controller that initialize the menu for the setting and launch a
     * game And other attribut in the class
     *
     * @param controller Controller: the controller to call the method
     * @param model Model: the Observer class
     */
    public View(Controller controller, Game game) {
        pcs = new PropertyChangeSupport(this);
        this.controller = controller;
        this.game = game;
        this.other = new OtherView(controller, game);
        
        this.secondStage = new Stage();
        
        settingGame = new GameSetting();
        boardFX = new Board();
        gameFX = new GameFX(this);
        
        this.game.addPropertyChangeListener(this);
        
        settingGame.addPropertyChangeListener(this);
        boardFX.addPropertyChangeListener(this);
        gameFX.addPropertyChangeListener(this);
    }

    /**
     * Initialize all the JavaFx view of the game with all the component Add
     * inside the action of the button
     *
     * @param primaryStage Stage: the stage that contain all the component to
     * show it
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SameGame");
        
        root = new StackPane();
        root.setPrefSize(1000, 750);
        
        this.primaryStage = primaryStage;
        
        BorderPane settingPane = settingGame.getGameSetting();
        root.getChildren().add(settingPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void modifyScene(List<Object> infoBoard){
        int[] widthHeight = controller.getWidthHeight();
        verifBall = new boolean[widthHeight[0]][widthHeight[1]];
        boardPane = gameFX.getGameSetting(infoBoard, boardFX);
        root.getChildren().add(boardPane);
        root.getChildren().remove(root.getChildren().size()-2);
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
            case GameSetting.PROPERTY_BUTTON_SETTING_PLAY:
                controller.play(evt.getNewValue());
                break;
            case Game.PROPERTY_START:
                infoBoard = (List<Object>) evt.getNewValue();
                modifyScene(infoBoard);
                other.start(secondStage, infoBoard);
                break;
            case Game.PROPERTY_DELETE_BALL:
            case Game.PROPERTY_DELETE_UNDO:
                infoBoard = (List<Object>) evt.getNewValue();
                modifyScene(infoBoard);
                break;
            case Board.PROPERTY_CHECK_POSITION:
                Point positionBall = (Point) evt.getNewValue();
                controller.play(GameFX.PROPERTY_ACTION_DELETE+" "+ positionBall.getX() + " " + positionBall.getY());
                break;
            case Board.PROPERTY_HOVER_CHECK_POSITION:
                Point positionBallHover = (Point) evt.getNewValue();
                if(!checkIfPositionIsAlreadyVerif(positionBallHover)){
                    int[] widthHeight = controller.getWidthHeight();
                    verifBall = new boolean[widthHeight[0]][widthHeight[1]];
                    controller.setState(State.CHECK_BALL_HOVER);
                    controller.play(positionBallHover);
                }
                break;
            case Game.PROPERTY_GET_VERIF_BALL:
                verifBall = (boolean[][]) evt.getNewValue();
                boardFX.illuminateBall(verifBall);
                break;
            case GameFX.PROPERTY_ACTION_UNDO:
                controller.play(GameFX.PROPERTY_ACTION_UNDO);
                break;
            case GameFX.PROPERTY_ACTION_REDO:
                controller.play(GameFX.PROPERTY_ACTION_REDO);
                break;
            case GameFX.PROPERTY_ACTION_RESTART:
               controller.setState(State.GAME_END_OVER);
               controller.play(0);
                break;
            case GameFX.PROPERTY_ACTION_STOP:
                controller.setState(State.GAME_FINISH);
                break;
            case Game.PROPERTY_GAME_ERROR:
//                String[] errors = (String[]) evt.getNewValue();
//                System.out.print("Erreur : ");
//                for(var error:errors){
//                    System.out.print(error + " : ");
//                }
//                System.out.println("");
                break;
            case Game.PROPERTY_GAME_WIN:
                infoBoard = (List<Object>) evt.getNewValue();
                modifyScene(infoBoard);
                change(PROPERTY_GAME_OVER, "Vous avez gagné !");
                break;
            case Game.PROPERTY_GAME_LOOSE:
                infoBoard = (List<Object>) evt.getNewValue();
                modifyScene(infoBoard);
                change(PROPERTY_GAME_OVER, "Vous avez perdu !");
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
