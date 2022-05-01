/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerConsole;

import model.Game;
import model.dpCommand.Command;
import model.dpCommand.FactoryCommand;
import model.dpCommand.action.Help;
import viewConsole.View;

/**
 * Controller of the game, it iniliaze the all game. Initiliaze all the state of
 * the game and make a loop to continue everytime
 *
 * @author basile <54018@etu.he2b.be>
 */
public class Controller {

    private View view;
    private Game game;
    private int[] settingGame;

    /**
     * Create a controller that can initialize the all game for player And make
     * all the view for the home page and the board game.
     *
     * @param view View: view for the display of the game
     * @param model Model: model for the all game that will be initialize
     */
    public Controller(View view, Game game) {
        this.game = game;
        this.view = view;

    }

    /**
     * Start the game by requesting Number of player in the game and start the
     * game for all the player
     */
    private void startGame() {
        settingGame = view.askAttributBoard();
        game.start(settingGame);
        view.displayInformationGame();
    }

    /**
     * Restart the game by relaunch the game with the default setting
     */
    private void restartGame() {
        view.displayEndGame();
        game.start(settingGame);
        view.displayInformationGame();
    }

    /**
     * Ask the command to launch and switch it
     */
    private void actionCommand() {
        switchCommand(view.askQuestion());
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
        } else {
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
                        String error[] = {"\nIl n'existe pas de commande encore. Créée en une pour pouvoir faire la commande", "redo ne peut pas fonctionner car il n'y a pas encore de commande"};
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
            view.displayInformationGame();
        }
    }

    /**
     * Executes a given Command object, and adds it to the command history
     *
     * @param command Command : the command that will be store and execute
     * privateremoveCommandHistoryUndo : the command that will be store and
     * execute
     */
    private void executeCommand(Command command) {
        game.addCommandHistoryUndo(command);
        command.execute();
    }

    /**
     * Undoes the last executed command and removes it from the command history
     */
    private void undoCommand(Command command) {
        command.unexecute();
        game.removeCommandHistoryUndo(command);
        game.addCommandHistoryRedo(command);
    }

    /**
     * Execute the command past in parameter
     *
     * @param command Command: that will be execute
     */
    private void redoCommand(Command command) {
        command.execute();
        game.removeCommandHistoryRedo(command);
        game.addCommandHistoryUndo(command);
    }

    /**
     * Create all the current view and detail of the game Display a welcome
     * message with all stuff For each State it will make a different action the
     * action of the game is Start - pickTile - place_or_drop_tile - dropTile or
     * askPosition - nextPlayer and gameOver It will loop all the switch such as
     * the user force the closure of the programm
     */
    public void play() {
        boolean isFinish = false;
        while (!isFinish) {
            switch (game.getState()) {
                case NOT_STARTED:
                    startGame();
                    break;
                case PICK_DELETE_BALL:
                    actionCommand();
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
