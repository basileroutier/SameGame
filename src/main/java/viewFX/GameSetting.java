/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewFX;

import controller.Controller;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Ball;
import model.ColorBall;


/**
 *
 * @author basile
 */
public class GameSetting {

    private PropertyChangeSupport pcs;
    private ColorBall color;

    private BorderPane center;
    private VBox settingPersoGameCenter;

    private Button startToPlay;
    private ComboBox comboBoxArrayPerso;
    private ComboBox comboBoxDifficulties;
    private ComboBox comboBoxWidth;
    private ComboBox comboBoxHeight;
    private Label lblWidth;
    private Label lblHeight;
    private Label lblDiff;
    
    public final static String PROPERTY_BUTTON_SETTING_PLAY="view.GameSetting.buttonPlay";

    /*
     * Controller that initialize the Observer class
     */
    public GameSetting() {
        pcs = new PropertyChangeSupport(this);
    }
    
    public BorderPane getGameSetting() {

        center = new BorderPane();
        center.setMaxWidth(320);
        center.setMaxHeight(320);
        center.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox settingGameTop = new VBox();
        Label lblTitle = new Label("Selectionné le plateau :");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 15));
        ObservableList<String> optionsTableau
                = FXCollections.observableArrayList(
                        "1. Tableau de 12*16 avec 4 couleurs",
                        "2. Tableau personnalisé"
                );
        comboBoxArrayPerso = new ComboBox(optionsTableau);
        comboBoxArrayPerso.getSelectionModel().select(0);
        settingGameTop.getChildren().addAll(lblTitle, comboBoxArrayPerso);
        settingGameTop.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(lblTitle, new Insets(15, 0, 3, 0));
        VBox.setMargin(comboBoxArrayPerso, new Insets(5, 0, 3, 0));

        settingPersoGameCenter = new VBox();
        ObservableList<Integer> optionsTailleTableauLine = FXCollections.observableArrayList();
        ObservableList<Integer> optionsTailleTableauCol = FXCollections.observableArrayList();
        ObservableList<Integer> optionsDifficulties = FXCollections.observableArrayList();
        for (var i = 10; i <= 18; i++) {
            optionsTailleTableauLine.add(i);
        }
        for (var i = 10; i <= 20; i++) {
            optionsTailleTableauCol.add(i);
        }
        for (var i = 3; i <= 5; i++) {
            optionsDifficulties.add(i);
        }
        lblWidth = new Label("Nombre de lignes :");
        lblTitle.setFont(Font.font("System", FontWeight.NORMAL, 15));
        lblHeight = new Label("Nombre de colonnes :");
        lblTitle.setFont(Font.font("System", FontWeight.NORMAL, 15));
        lblDiff = new Label("Difficultés :");
        lblTitle.setFont(Font.font("System", FontWeight.NORMAL, 15));
        comboBoxWidth = new ComboBox(optionsTailleTableauLine);
        comboBoxHeight = new ComboBox(optionsTailleTableauCol);
        comboBoxDifficulties = new ComboBox(optionsDifficulties);
        comboBoxWidth.getSelectionModel().select(0);
        comboBoxHeight.getSelectionModel().select(0);
        comboBoxDifficulties.getSelectionModel().select(0);

        settingPersoGameCenter.getChildren().addAll(lblWidth, comboBoxWidth, lblHeight, comboBoxHeight, lblDiff, comboBoxDifficulties);
        settingPersoGameCenter.setAlignment(Pos.CENTER);

        VBox.setMargin(comboBoxWidth, new Insets(5, 0, 3, 0));
        VBox.setMargin(comboBoxHeight, new Insets(5, 0, 3, 0));
        VBox.setMargin(comboBoxDifficulties, new Insets(5, 0, 3, 0));

        VBox launchGame = new VBox();

        startToPlay = new Button("Commencer à jouer");
        launchGame.getChildren().add(startToPlay);
        launchGame.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(startToPlay, new Insets(5, 0, 15, 0));

        center.setTop(settingGameTop);
        center.setCenter(settingPersoGameCenter);
        center.setBottom(launchGame);
        setVisibleButtonChoice(false);

        changeListenerArrayPerso();
        isStartButton();
        return center;
    }

    private void changeListenerArrayPerso() {
        comboBoxArrayPerso.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals("Two")) {
                if (newVal.toString().charAt(0) == '1') {
                    setVisibleButtonChoice(false);
                } else {
                    setVisibleButtonChoice(true);
                }
            }
        });
    }

    /**
     * Set action on the longest button and notify all the observable if it is
     * click
     */
    private void isStartButton() {
        startToPlay.setOnAction(e -> verifySelectionStart());
    }

    private void verifySelectionStart() {
        if (comboBoxArrayPerso.getValue().toString().charAt(0) == '1') {
            change(PROPERTY_BUTTON_SETTING_PLAY, 0);
        } else {
            int line = (int) comboBoxWidth.getValue();
            int height = (int) comboBoxHeight.getValue();
            int difficulties = (int) comboBoxDifficulties.getValue();
            change(PROPERTY_BUTTON_SETTING_PLAY, line, height, difficulties);
        }
        center.setDisable(true);
        center.setVisible(false);
    }

    private void setVisibleButtonChoice(boolean setVisible) {
        if (setVisible) {
            settingPersoGameCenter.setVisible(true);
        } else {
            settingPersoGameCenter.setVisible(false);
        }

    }

    /**
     * Add an observable in the PropertyChange to Observe the current class
     * @param listener PropertyChangeListener: the Listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove an observable in the PropertyChange to Observe the current class
     * @param listener PropertyChangeListener: the Listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    
    /**
     * Notify all the class that Observe the current class from a modification
     * @param buttonClick String: message to send to know which things need to be modify
     * @param settingGame int[]: array of all the value for the setting of the game
     */
    public void change(String buttonClick, int... settingGame) {
        pcs.firePropertyChange(buttonClick, null, settingGame);
    }
}
