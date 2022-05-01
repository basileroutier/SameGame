/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dpCommand;

import model.Game;
import model.Point;
import model.dpCommand.action.DeleteBall;
import model.dpCommand.action.Help;
import viewConsole.View;

/**
 *
 * @author basile
 */
public class FactoryCommand {

    private String[] commande;    
    private Game game;

    /**
     * Simple constructor with the attribut
     *
     * @param commande String[]: all the command
     * @param game GameFacade: current game with all action facade
     */
    public FactoryCommand(String[] commande, Game game) {
        this.commande = commande;
        this.game = game;
    }

    /**
     * Check if the position is a digit and if the position is inside the board
     * And if the ball have neighbor. If the condition is not respect a Help command
     * Is return Else it return the a deleteBall comamnd for delete the ball
     * In the game
     * @return a deleteBall command if the condition is respect else help command
     */
    public Command deleteBallCommand() {
        Command isDigit = isCharacterDigit(1,2);
        if (isDigit!=null) {
            return isDigit;
        }
        int positionX = (int) Integer.parseInt(commande[1]);
        int positionY = (int) Integer.parseInt(commande[2]);
        if (!checkPositionXY(positionX, positionY)) {
            String error[] = {"\nLes positions rentrées ne sont pas correct", 
                "Le format est : une positionX + une positionY qui soit dans le tableau"};
            Command help = new Help(game,error);
            return help;
        }
        --positionX;
        --positionY;
        Point position = new Point(positionX, positionY);
        Command isBallHaveNeighbor = isBallNeighbor(position);
        if (isBallHaveNeighbor != null) {
            return isBallHaveNeighbor;
        }
        return new DeleteBall(game, position);
    }

    /**
     * Check if a ball have neighbor if not it return a help command
     * Else return null
     * @param position Point: position of the selected ball
     * @return null if ball have neighbor else help command
     */
    private Command isBallNeighbor(Point position) {
        if (!game.isBallHaveNeighbor(position)) {
            String error[] = {"\nLa balle n'a pas de voisin ou a déjà été enlevé", "Réessayer avec une balle qui soit correct"};
            Command help = new Help(game,error);
            return help;
        }
        return null;
    }

    /**
     * Check if the positionX and positionY is respect return true if the
     * conditon is respect else return false
     *
     * @param posX int: the positionX in the command list
     * @param posY int: the positionY in the command list
     * @return true if the condition position is respect else false
     */
    private boolean checkPositionXY(int posX, int posY) {
        if (posX <= 0 || posX > game.getLine()) {
            return false;
        }
        if (posY <= 0 || posY > game.getColumn()) {
            return false;
        }
        return true;
    }

    public boolean isDigit() {
        for (var i = 1; i < commande.length; i++) {
            for (var j = 0; j < commande[j].length(); j++) {
                if (!Character.isDigit(commande[i].charAt(j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Check if the array number of command is a character digit If it is not,
     * it return an help Else return null if their is digit thing
     *
     * @param command int[]: number of the command
     * @return a command if the character is not a digit else return null
     */
    private Command isCharacterDigit(int... command) {
        for (var numberCommand : command) {
            for (var j = 0; j < commande[numberCommand].length(); j++) {
                if (!Character.isDigit(commande[numberCommand].charAt(j))) {
                    String error[] = {"Veuillez entrez un nombre et non pas autre chose", "ne doit pas être un chiffre. Réessayer avec un chiffre"};
                    Command help = new Help(game,error);
                    return help;
                }
            }
        }
        return null;
    }
}
