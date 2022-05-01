/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static model.NeighborBall.*;
import model.dpCommand.Command;

/**
 * Start the game with the bestScore and the state to start the game. 
 * Make different action to advance in a board of the ball
 * @author basile
 */
public class Game {

    private PropertyChangeSupport pcs;
    private Board board;
    private State state;
    private int numberOfColor;
    private int score;
    private int bestScore;
    private List<Command> historyUndo;
    private List<Command> historyRedo;
    
    public final static String PROPERTY_START = "model.Game.start";
    public final static String PROPERTY_DELETE_BALL = "model.Game.deleteBall";
    public final static String PROPERTY_GET_VERIF_BALL = "model.Game.checkPosition";
    public final static String PROPERTY_DELETE_UNDO = "model.Game.deleteBallUndo";
    public final static String PROPERTY_GAME_WIN = "model.Game.gameWin";
    public final static String PROPERTY_GAME_LOOSE = "model.Game.gameLoose";
    public final static String PROPERTY_GAME_FINISH = "model.Game.gameFinish";
    public final static String PROPERTY_GAME_ERROR = "model.Game.gameError";

    /**
     * Simple constructor that initialize state and best score
     * With notify observer pattern
     */
    public Game() {
        this.state = State.NOT_STARTED;
        this.bestScore = 0;
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Initialize the game depending the number of argument passed.
     * If the number of argument is 1 then it initialize default board
     * Else it initialize with the personnalize difficulties
     * @param initAttribut int[]: array of the size
     */
    private void initializeBoard(int... initAttribut) {
        switch (initAttribut.length) {
            case 1:
                this.board = new Board();
                this.numberOfColor = 4;
                break;
            case 3:
                int line = initAttribut[0];
                int column = initAttribut[1];
                int numberColorChoice = initAttribut[2];
                int tailleMin = 10;
                int tailleMaxLine = 18;
                int tailleMaxColumn = 20;
                if (line < tailleMin || line > tailleMaxLine || column < tailleMin || column > tailleMaxColumn) {
                    throw new IllegalArgumentException("La longueur et largeur doit être supérieur à " + tailleMin + " et inférieur à " + tailleMaxColumn + " ou " + tailleMaxLine + " pour les lignes");
                }
                if (numberColorChoice < 3 || numberColorChoice > 5) {
                    throw new IllegalArgumentException("Le nombre de couleur doit être compris entre 3 et 5");
                }
                this.board = new Board(line, column);
                this.numberOfColor = numberColorChoice;
                break;
            default:
                throw new IllegalArgumentException("Le nombre de valeur pour l'initialisation de la partie n'est pas correct");
        }
        initializeBalls(board.getLine(), board.getColumn());
        state = State.PICK_DELETE_BALL;
        List<Object> infoBoard = List.of(board.getBalls(), score, bestScore, getNumberRemainBall());
        change(PROPERTY_START, infoBoard);
    }
    
    /**
     * Initialize the ball array of the board with the number of color choose
     * @param line int: number of line
     * @param column int: number of column
     */
    private void initializeBalls(int line, int column) {
        this.score = 0;
        historyUndo = new ArrayList<>();
        historyRedo = new ArrayList<>();
        Random random = new Random();
        for (var i = 0; i < line; i++) {
            for (var j = 0; j < column; j++) {
                ColorBall color = ColorBall.values()[random.nextInt(numberOfColor)];
                Point position = new Point(i, j);
                board.addBall(position, color);
            }
        }

    }

    /**
     * Start state :
     * if the state is not not_started or game_end_over it throw an exception
     * else it initialize the board
     * @param initAttribut int[]: array of the size or default board
     */
    public void start(int... initAttribut) {
        switch (state) {
            case NOT_STARTED:
            case GAME_END_OVER:
                initializeBoard(initAttribut);
                break;
            case PICK_DELETE_BALL:
            case GAME_FINISH:
            case CHECK_BALL_HOVER:
                throw new IllegalStateException("It's is not possible to start the game at this state");
            default:
                throw new AssertionError(state.name());
        }
    }

    /**
     * Start state :
     * if the state is not pick_delete_ball it throw an exception
     * @param position Point: position of the selected ball
     */
    public void pickBall(Point position) {
        switch (state) {
            case PICK_DELETE_BALL:
                verifEndDeleteBall(position);
                break;
            case NOT_STARTED:
            case CHECK_BALL_HOVER:
            case GAME_END_OVER:
            case GAME_FINISH:
                throw new IllegalStateException("It's is not possible to start the game at this state");
            default:
                throw new AssertionError(state.name());
        }
    }

    /**
     * Throw an exception if the condition of the position correct or
     * If the ball is not a ball
     * If the ball doesn't have neighbor
     * @param position Point: the position of the selected ball
     */
    protected void checkPickBall(Point position) {
        if (!isPositionCorrect(position)) {
            throw new IllegalArgumentException("La position n'est pas comprise dans le tableau");
        }
        if (!board.isBall(position)) {
            throw new IllegalArgumentException("La position rentré n'est pas une bille");
        }
        if (!isNeigbhorExist(board)) {
            throw new IllegalArgumentException("La position rentré n'a pas de voisin");
        }
    }

    /**
     * Increment the current score with the specific calcul
     * @param numberDeleteBalls int: number of delete ball
     */
    private void incrementScore(int numberDeleteBalls) {
        score += (numberDeleteBalls * (numberDeleteBalls - 1));
    }

    
    /**
     * Verif the selected ball and delete the ball if all is correct.
     * It check if it is the end of if the game is loose.
     * If it is, it change the statut and game over the game
     * Else
     * increment the score and continue
     * @param position Point: selected ball
     */
    public void verifEndDeleteBall(Point position) {
        board.getNeighborBalls(position);
        checkPickBall(position);
        int numberDeleteBalls = board.deleteBall();
        incrementScore(numberDeleteBalls);
        if (isWinGame() || isLooseGame()) {
            state = State.GAME_END_OVER;
            gameOver();
            List<Object> infoBoard = List.of(board.getBalls(), score, bestScore, getNumberRemainBall());
            if(isWinGame()){
                change(PROPERTY_GAME_WIN, infoBoard);
            }else{
                change(PROPERTY_GAME_LOOSE, infoBoard);
            }
            
        }else{
            List<Object> infoBoard = List.of(board.getBalls(), score, bestScore, getNumberRemainBall());
            change(PROPERTY_DELETE_BALL, infoBoard);
        }
    }

    /**
     * Check if the state is different that game over, if it is
     * It throw an exception state
     * else
     * It set bestscore if score is higher than bestscore
     */
    protected void gameOver() {
        if (state != State.GAME_END_OVER) {
            throw new IllegalStateException("It's is not possible to end the game at this state");
        }
        if (score > bestScore) {
            bestScore = score;
        }
    }

    /**
     * Set the state to game_finish
     */
    public void gameFinish() {
        state = State.GAME_FINISH;
    }

    /**
     * Change the board with the board giving in parameter
     * @param boardOriginal Board: original board
     * @param score score: original score
     */
    public void changeBoard(Ball[][] boardOriginal, int score) {
        board.setBoard(boardOriginal);
        this.score = score;
        List<Object> infoBoard = List.of(board.getBalls(), score, bestScore, getNumberRemainBall());
        change(PROPERTY_DELETE_UNDO, infoBoard);
    }

    /**
     * Return if the position is correct.
     * @param position Point: position of the selected ball
     * @return true if the position is correct else false
     */
    private boolean isPositionCorrect(Point position) {
        return board.isPositionCorrect(position);
    }

    /**
     * Check if the selected ball have neighbor
     * If the position is not correct and if it is not a ball, it return false
     * Else return true or false if the ball have neighbor
     * @param position Point: position of the selected ball
     * @return true if the ball have neighbor else false
     */
    public boolean isBallHaveNeighbor(Point position) {
        if (!isPositionCorrect(position) || !board.isBall(position)) {
            return false;
        }
        board.getNeighborBalls(position);
        return isNeigbhorExist(board);
    }

    /**
     * Get the verif ball of the position of the ball
     * @param position Point: position of the selected ball
     */
    public void getVerifArrayPosition(Point position) {
        if (!isBallHaveNeighbor(position)) {
            notifyHelp("La balle n'a pas de voisin");
        } else {
            change(PROPERTY_GET_VERIF_BALL, board.getVerifBall());
        }
    }

    /**
     * Get the color of the selected ball
     * @param position Point: position of the ball
     * @return the color of the selected ball
     */
    public char getColorBall(Point position) {
        return board.getColorBall(position);
    }

    /**
     * Check if their is no more ball in the game
     * return true if their is no more ball in the game
     * else false
     * @return true if their is no more ball in the game else false
     */
    protected boolean isWinGame() {
        for (var i = 0; i < board.getBalls().length; i++) {
            for (var j = 0; j < board.getBalls()[0].length; j++) {
                if (board.getBalls()[i][j]!= null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a ball have neigbhor
     * return true if their is no more neighbor
     * else false
     * @return true if their is no more neighbor for ball in the game else false
     */
    protected boolean isLooseGame() {
        for (var i = 0; i < board.getBalls().length; i++) {
            for (var j = 0; j < board.getBalls()[0].length; j++) {
                if (isBallHaveNeighbor(new Point(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Add a Ball to a specific position
     * FOR TESTING ONLY
     */
    protected void addBall(Point position, ColorBall color){
        board.addBall(position, color);
    }

    /**
     * If the current state is the same given one. If it is it throw an exception
     * Else it change the state
     * @param state 
     */
    public void setState(State state) {
        if (this.state == state) {
            throw new IllegalArgumentException("Il n'est pas possible de modifier l'état actuelle du au contrainte");
        }
        this.state = state;
    }

    /**
     * Add a command to the historyUndo
     *
     * @param command Command: command that will be added
     */
    public void addCommandHistoryUndo(Command command) {
        historyUndo.add(command);
    }

    /**
     * Remove a command to the historyUndo
     *
     * @param command Command: command that will be removed
     */
    public void removeCommandHistoryUndo(Command command) {
        historyUndo.remove(command);
    }

    /**
     * Add a command to the historyUndo
     *
     * @param command Command: command that will be added
     */
    public void addCommandHistoryRedo(Command command) {
        historyRedo.add(command);
    }

    /**
     * Remove a command to the historyUndo
     *
     * @param command Command: command that will be removed
     */
    public void removeCommandHistoryRedo(Command command) {
        historyRedo.remove(command);
    }

    /**
     * Get the last command of undo
     * And return it
     * @return the last command of undo
     */
    public Command getLastCommandUndo() {
        return historyUndo.get(historyUndo.size() - 1);
    }

    /**
     * Get the last command of redo
     * And return it
     * @return the last command of redo
     */
    public Command getLastCommandRedo() {
        return historyRedo.get(historyRedo.size() - 1);
    }

    /**
     * Return the array of balls of the board
     * @return the balls array
     */
    public Ball[][] getBalls() {
        return board.getBalls();
    }

    /**
     * return the line of the board
     * @return the line of the board
     */
    public int getLine() {
        return board.getLine();
    }

    /**
     * return the column of the board
     * @return the column of the board
     */
    public int getColumn() {
        return board.getColumn();
    }
    
    /**
     * Return the number of color in the board
     * @return the number of color in the board
     */
    public int getNumberColor(){
        return numberOfColor;
    }

    /**
     * Get the current state of the game
     * @return the current state of the game
     */
    public State getState() {
        return state;
    }

    /**
     * Get the current score of the game
     * @return the current score of the game
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the current bestscore of the game
     * @return the current bestscore of the game
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Get the history of undo of the game
     * @return the history of undo of the game
     */
    public List<Command> getHistoryUndo() {
        return historyUndo;
    }

    /**
     * Get the history of redo of the game
     * @return the history of redo of the game
     */
    public List<Command> getHistoryRedo() {
        return historyRedo;
    }

    /**
     * Clear the redo history
     */
    public void clearRedoCommand() {
        historyRedo = new ArrayList<>();
    }
    
    /**
     * Get the number of remain ball
     * And return it
     * @return the number of remain ball
     */
    public int getNumberRemainBall(){
        Ball[][] boardBall = board.getBalls();
        int numberRemainBall = 0;
        for(var i=0;i<boardBall.length;i++){
            for(var j=0;j<boardBall[0].length;j++){
                if(boardBall[i][j]!=null){
                    numberRemainBall++;
                }
            }
        }
        return numberRemainBall;
    }

    /**
     * return the array of balls with the first character of color ball
     * @return the array of balls with the first character of color ball
     */
    public String toStringBalls() {
        StringBuilder ballsArray = new StringBuilder();
        for (var i = 0; i < board.getBalls().length; i++) {
            for (var j = 0; j < board.getBalls()[0].length; j++) {
                ballsArray.append(board.getColorBall(new Point(i, j))).append(" ");
            }
            ballsArray.append("\n");
        }
        return ballsArray.toString();
    }

    /**
     * Add listener to the observator
     * @param listener PropertyChangeListener: the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove listener to the observator
     * @param listener PropertyChangeListener: the listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Notify all the observator with a specific message and content
     * @param message String: message
     * @param objectSend Object: content to send
     */
    public void change(String message, Object objectSend) {
        pcs.firePropertyChange(message, null, objectSend);
    }

    /**
     * Notify the observator when an error appear
     * @param error String[]: all the error appear
     */
    public void notifyHelp(String... error) {
        change(PROPERTY_GAME_ERROR, error);
    }

}
