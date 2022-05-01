/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javafx.stage.Stage;
import model.Game;
import model.Point;
import model.State;
import model.dpCommand.Command;
import model.dpCommand.FactoryCommand;
import model.dpCommand.action.DeleteBall;
import model.dpCommand.action.Help;
import viewFX.View;
import viewFX.GameSetting;

/**
 * Controller that contain all the game launch
 * @author basile
 */
public class Controller {

    private Game game;
    private View view;
    private boolean isFinish;
    private int[] settingGame;

    /**
     * Basic controller that initialize the model, the view And call the view to
     * show the stage
     *
     * @param game Model: the model of the game
     * @param primaryStage Stage: the stage which is used by the View
     */
    public Controller(Game game, Stage primaryStage) {
        this.game = game;
        this.view = new View(this, game);
        this.view.start(primaryStage);
    }

    /**
     * Start the game by initialize attribut and launch the game with the setting
     * @param settingGame int[]: setting of the game
     */
    private void start(int... settingGame) {
        this.isFinish = false;
        this.settingGame = settingGame;
        game.start(settingGame);
    }

    /**
     * Restart the game by relaunch the game with the default setting
     */
    private void restartGame() {
        this.isFinish = false;
        game.start(settingGame);
    }

    /**
     * Get the command to launch
     * @param choixAction String: the action to make
     */
    private void switchCommand(String choixAction) {
        String[] commandeChoix = choixAction.split(" ");
        FactoryCommand factCommand = new FactoryCommand(commandeChoix, game);
        commandAction(commandeChoix, factCommand);
    }

    /**
     * Check which command was been selected and check if the command is not null before checking it
     * If a problem appear, an help command is execute
     * @param commande String[]: decompose command
     * @param factCommand FactoryCommand: the command factory for deleteBall
     */
    private void commandAction(String[] commande, FactoryCommand factCommand) {
        if (commande.length < 1) {
            String error[] = {"\nL'action réalisé n'existe pas...", "une action qui soit correct"};
            Help helpDef = new Help(game, error);
            helpDef.execute();
        }
        String action = commande[0];
        switch (action) {
            case "delete":
                if (commande.length != 3) {
                    String error[] = {"\nLa commande rentrer doit avoir 3 paramètres", "Celui ci contient. La commande + la position X + la position Y"};
                    Command help = new Help(game, error);
                    help.execute();
                    break;
                }
                executeCommand(factCommand.deleteBallCommand());
                break;
            case "undo":
                if (game.getHistoryUndo().isEmpty()) {
                    String error[] = {"\nIl n'existe pas de commande encore. Créée en une pour pouvoir faire la commande", "undo ne peut pas fonctionner car il n'y a pas encore de commande"};
                    Command help = new Help(game, error);
                    help.execute();
                    break;
                }
                Command commandUndo = game.getLastCommandUndo();
                undoCommand(commandUndo);
                break;
            case "redo":
                if (game.getHistoryRedo().isEmpty()) {
                    String error[] = {"\nIl n'existe pas de commande encore. Créée en une pour pouvoir faire la commande", "undo ne peut pas fonctionner car il n'y a pas encore de commande"};
                    Command help = new Help(game, error);
                    help.execute();
                    break;
                }
                Command commandRedo = game.getLastCommandRedo();
                redoCommand(commandRedo);
                break;
            case "stop":
                game.gameFinish();
                String errorStop[] = {"\nVous avez décidé de terminer le jeu ici.", "Bonne journée :)"};
                Help helpStop = new Help(game, errorStop);
                helpStop.execute();
                break;
            default:
                String error[] = {"\nL'action réalisé n'existe pas...", "une action qui soit correct"};
                Help helpDef = new Help(game, error);
                helpDef.execute();
        }
    }

    /**
     * Executes a given Command object, and adds it to the command history
     *
     * @param command Command : the command that will be store and execute
     */
    private void executeCommand(Command command) {
        command.execute();
        game.clearRedoCommand();
        game.addCommandHistoryUndo(command);
    }

    /**
     * Undoes the last executed command and removes it from the command history
     */
    private void undoCommand(Command command) {
        command.unexecute();
        game.addCommandHistoryRedo(command);
        game.removeCommandHistoryUndo(command);
    }

    /**
     * Execute the command past in parameter
     *
     * @param command Command: that will be execute
     */
    private void redoCommand(Command command) {
        command.execute();
        game.addCommandHistoryUndo(command);
        game.removeCommandHistoryRedo(command);
    }
    

    /**
     * Set the state to a new one if the current state is different than the new one
     * @param state State: new state
     */
    public void setState(State state) {
        if (game.getState() != state) {
            game.setState(state);
        }
    }

    /**
     * Get the width and height of the game and return it
     * @return the width and height of the game
     */
    public int[] getWidthHeight() {
        int[] widthHeight = {game.getLine(), game.getColumn()};
        return widthHeight;
    }

    /**
     * Create all the current view and detail of the game Display a welcome
     * message with all stuff For each State it will make a different action the
     * action of the game is Start - pickTile - place_or_drop_tile - dropTile or
     * askPosition - nextPlayer and gameOver It will loop all the switch such as
     * the user force the closure of the programm
     *
     * @param command
     */
    public void play(Object command) {
        if (!isFinish) {
            switch (game.getState()) {
                case NOT_STARTED:
                    start((int[]) command);
                    break;
                case CHECK_BALL_HOVER:
                    game.getVerifArrayPosition((Point) command);
                    setState(State.PICK_DELETE_BALL);
                    break;
                case PICK_DELETE_BALL:
                    switchCommand((String) command);
                    break;
                case GAME_END_OVER:
                    restartGame();
                    break;
                case GAME_FINISH:
                    isFinish = true;
                    break;
                default:
                    throw new AssertionError(game.getState().name());
            }
        }
    }

}
