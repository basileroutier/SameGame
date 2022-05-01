/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewConsole;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Scanner;
import model.Board;
import model.Game;
import model.Point;
import model.dpCommand.Command;
import model.dpCommand.FactoryCommand;

/**
 * The console view
 * @author basile
 */
public class View implements PropertyChangeListener {

    private Game game;

    /**
     * simple constructor
     * @param game Game: the game
     */
    public View(Game game) {
        this.game = game;
        this.game.addPropertyChangeListener(this);
    }

    /**
     * Ask for the setting of the game
     * Check if the putted number is valid
     * And return the choice
     * @return the choice for the setting
     */
    public int[] askAttributBoard() {
        Scanner sc = new Scanner(System.in);
        String messageAction = "\nVeuillez choisir différentes valeurs :";
        String allAction = messageTabulation("Entrée 0 pour créer un jeu de 12,16 avec 4 couleur"
                + "\n\t* Sinon entrée : le nombre de ligne + le nombre de colonne + nombre de couleurs "
                + "\n\t* Si vous entrez une chaine de caractère mauvaise ou pas le bon nombre min/max de ligne et colonne, une version par défaut vous sera donnée."
                + "\n\t* La taille maximum de longueur/largeur est de 18 lignes et 20 colonnes");
        System.out.println(messageAction);
        System.out.println(allAction);
        String choixAction = verifRobustReadString(sc, "Vous n'avez pas choisie une bonne chaine", messageAction, allAction);
        String[] commandeChoix = choixAction.split(" ");
        if (!isDigit(commandeChoix)) {
            int defaultVal[] = {0};
            return defaultVal;
        }
        if (commandeChoix.length != 3 || isNotLineColumnColor(commandeChoix)) {
            int defaultVal[] = {0};
            return defaultVal;
        }

        return convertStringToInt(commandeChoix);
    }

    /**
     * check if the position enter is in the board.
     * return true if it is, else false
     * @param commande String[]: array of normally number
     * @return true if the position is inside the board else false
     */
    private boolean isNotLineColumnColor(String[] commande) {
        int tailleMin = 10;
        int tailleMax = 30;
        int line = Integer.parseInt(commande[0]);
        int column = Integer.parseInt(commande[1]);
        int numberColor = Integer.parseInt(commande[2]);
        return (line < tailleMin || line > tailleMax || column < tailleMin || column > tailleMax || numberColor < 3 || numberColor > 5);

    }

    /**
     * Convert the String character to int
     * And return it
     * @param valeurs String[] array of normally number
     * @return the string that have been converted to int
     */
    private int[] convertStringToInt(String[] valeurs) {
        int[] choixValeurs = new int[valeurs.length];
        for (var i = 0; i < valeurs.length; i++) {
            choixValeurs[i] = Integer.parseInt(valeurs[i]);
        }
        return choixValeurs;
    }

    /**
     * Check if a string have digit inside their things
     * return true if the array contain only digit else false
     * @param commande String[]: array of normaly number
     * @return true if the array contain only digit else false
     */
    public boolean isDigit(String[] commande) {
        for (var i = 0; i < commande.length; i++) {
            for (var j = 0; j < commande[i].length(); j++) {
                if (!Character.isDigit(commande[i].charAt(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Display the color of the ball at a given position
     * @param position Point: position of the selected ball
     */
    public void getColorOfBall(Point position) {
        System.out.println(game.getColorBall(position));
    }

    /**
     * Display the information of the game
     */
    public void displayInformationGame() {
        System.out.println("---- Score : " + game.getScore() + " | Meilleure Score : " + game.getBestScore());
        System.out.println(game.toStringBalls());
    }

    /**
     * Ask the different action to do.
     * And return it if the robusteness has been passed
     * @return 
     */
    public String askQuestion() {
        Scanner sc = new Scanner(System.in);
        String messageAction = "\nVeuillez choisir entre les différentes actions :";
        String allAction = messageTabulation("delete pour supprimer une bille dans le jeu"
                + "\n\t* undo pour annuler la commande que vous venez de mettre"
                + "\n\t* redo pour refaire la commande que vous venez d'entrer "
                + "\n\t* stop pour finir le jeu et vous donnez les détails du jeu");
        System.out.println(messageAction);
        System.out.println(allAction);
        String choixAction = verifRobustReadString(sc, "Vous n'avez pas choisie une chaine de caractère", messageAction, allAction);
        return choixAction;
    }

    /**
     * Display the error that have been given
     * @param error String[]: array of error
     */
    public void displayHelp(String[] error) {
        for (var message : error) {
            displayError(message);
        }
    }

    /**
     * Display the end of the game
     */
    public void displayEndGame() {
        System.out.println("La partie est fini...");
        System.out.println("\t*Voici les statistiques du jeu...");
        System.out.println("Votre dernier score : " + game.getScore() + "  |  Le meilleure Score : " + game.getBestScore());
    }

    /**
     * Verify if the number is an Integer and not a chain, decimal or other
     * stuff If the user do not enter the right values, it display an error and
     * re ask the number Such as is a good value
     *
     * @param sc Scanner : get the value enter by user and gonna be compare
     * @param mutliScan Boolean : allow to know if Scanner need two
     * verificiation or only one
     * @param messageError Array String: contain all the error message
     * @return true when a number is valid so it is an Integer
     */
    private int verifRobustReadInt(Scanner sc, String... messageError) {
        while (!sc.hasNextInt()) {
            for (String message : messageError) {
                displayError(message);
            }
            sc.next();
        }
        return sc.nextInt();
    }

    /**
     * Verify if the number of the user is an double and not a chain, decimal or
     * other stuff If the user do not enter the right values, it display an
     * error and re ask the number Such as is a good value
     *
     * @param sc Scanner : get the value enter by user and gonna be compare
     * @param mutliScan Boolean : allow to know if Scanner need two
     * verificiation or only one
     * @param messageError Array String: contain all the error message
     * @return true when a number is valid so it is an Integer
     */
    private String verifRobustReadString(Scanner sc, String... messageError) {

        while (!sc.hasNext()) {
            for (String message : messageError) {
                displayError(message);
            }
            sc.nextLine();
        }

        return sc.nextLine();
    }

    /**
     * Display the error with a specific format The format is underline the
     * error message
     *
     * @param message String: message error that give in parameter to be display
     */
    private void displayError(String message) {
        System.out.println("\n" + message);
        for (var i = 0; i < message.length(); i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }

    /**
     * Return the string of tabulation to format the code
     *
     * @param message String: the message which will be formated
     * @return the string of tabulation to format the code
     */
    private String messageTabulation(String message) {
        return "\t* " + message;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventChange = evt.getPropertyName();
        List<Object> infoBoard;
        switch (eventChange) {
            case Game.PROPERTY_GAME_ERROR:
                String[] errors = (String[]) evt.getNewValue();
                displayHelp(errors);
                break;
            default:
                
        }

    }
    
}
