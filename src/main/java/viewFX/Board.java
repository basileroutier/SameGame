/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewFX;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Ball;
import model.ColorBall;
import model.Point;

/**
 *
 * @author basile
 */
public class Board {

    public final static String PROPERTY_CHECK_POSITION = "view.Board.checkPosition";
    public final static String PROPERTY_HOVER_CHECK_POSITION = "view.Board.hoverPosition";
    private PropertyChangeSupport pcs;
    private Button btnStart = new Button("Start");
    private Button[][] btnBall;

    public Board() {
        pcs = new PropertyChangeSupport(this);

    }

    public GridPane getMenuStart(Ball[][] balls) {
        GridPane buttonColor = new GridPane();
        btnBall = new Button[balls.length][balls[0].length];

        buttonColor.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        for (var i = 0; i < balls.length; i++) {
            for (var j = 0; j < balls[0].length; j++) {
                Button buttonBall = new Button("  ");
                if (balls[i][j] == null) {
                    buttonBall.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    buttonBall.setBorder(new Border(new BorderStroke(Color.rgb(100, 111, 122, 0.15), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                } else {
                    int[] rgb = hexToRbgaOpa();
                    buttonBall.setBackground(new Background(new BackgroundFill(Color.web(balls[i][j].getColor().code), CornerRadii.EMPTY, Insets.EMPTY)));
                    buttonBall.setBorder(new Border(new BorderStroke(Color.rgb(rgb[0], rgb[1], rgb[2], 0.15), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    buttonBall.setId(balls[i][j].getColor().toString());
                    
                    var positionBall = new Point((i + 1), (j + 1));
                    var positionBallHover = new Point(i, j);
                    
                    buttonBall.setOnAction(e -> change(PROPERTY_CHECK_POSITION, positionBall));
                    buttonBall.setOnMouseEntered(e -> change(PROPERTY_HOVER_CHECK_POSITION, positionBallHover));
                }
                
                btnBall[i][j] = buttonBall;
                buttonColor.add(btnBall[i][j], j, i);
            }
        }
        return buttonColor;
    }

    public void illuminateBall(boolean[][] verifBall) {
        for (var i = 0; i < verifBall.length; i++) {
            for (var j = 0; j < verifBall[0].length; j++) {
                if (btnBall[i][j] != null && btnBall[i][j].getId()!=null) {
                    if (!verifBall[i][j]) {
                        ColorBall color = ColorBall.valueOf(ColorBall.class, btnBall[i][j].getId());
                        ColorBall colorModif = color;
                        switch (color) {
                            case ILL_RED:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.RED.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.RED;
                                break;
                            case ILL_GREEN:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.GREEN.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.GREEN;
                                break;
                            case ILL_BLUE:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.BLUE.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.BLUE;
                                break;
                            case ILL_YELLOW:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.YELLOW.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.YELLOW;
                                break;
                            case ILL_ORANGE:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ORANGE.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ORANGE;
                                break;
                            default:
                        }
                        btnBall[i][j].setId(colorModif.toString());
                    }else{
                        ColorBall color = ColorBall.valueOf(ColorBall.class, btnBall[i][j].getId());
                        ColorBall colorModif = color;
                        switch (color) {
                            case RED:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ILL_RED.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ILL_RED;
                                break;
                            case GREEN:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ILL_GREEN.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ILL_GREEN;
                                break;
                            case BLUE:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ILL_BLUE.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ILL_BLUE;
                                break;
                            case YELLOW:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ILL_YELLOW.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ILL_YELLOW;
                                break;
                            case ORANGE:
                                btnBall[i][j].setBackground(new Background(new BackgroundFill(Color.web(ColorBall.ILL_ORANGE.code), CornerRadii.EMPTY, Insets.EMPTY)));
                                colorModif = ColorBall.ILL_ORANGE;
                                break;
                            default:
                        }
                        btnBall[i][j].setId(colorModif.toString());
                    }
                }
            }
        }
    }

    /**
     *
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    private int[] hexToRbgaOpa() {
        int hex = 0x123456;
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        int[] rgb = {r, g, b};
        return rgb;
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
    public void change(String buttonClick, Point position) {
        pcs.firePropertyChange(buttonClick, null, position);
    }
}
