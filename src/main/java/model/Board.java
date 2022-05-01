/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import static model.NeighborBall.*;

/**
 * Board that contain the balls and the neighbor of ball
 * Have some action on the board and utils for it
 * @author basile
 */
public class Board {

    private int line;
    private int column;
    private Ball[][] balls;
    private boolean[][] verifBalls;

    /**
     * Simple default constructor
     */
    public Board() {
        this(12, 16);
    }

    /**
     * Simple constructor with specify size
     * @param line int: number of line
     * @param column int: number of column
     */
    public Board(int line, int column) {
        this.line = line;
        this.column = column;
        this.balls = new Ball[line][column];
    }

    /**
     * Check if the position to add in the board is correct
     * @param position
     * @param color 
     */
    protected void addBall(Point position, ColorBall color) {
        isPositionException(position);
        balls[position.getX()][position.getY()] = new Ball(position, color);
    }

    /**
     * Return true if the position is in the board else false if not
     * @param position Point: position of the selected ball
     * @return true if the position is in the board else false if not
     */
    public boolean isPositionCorrect(Point position) {
        return ((position.getX() >= 0 && position.getX() < line) && (position.getY() >= 0 && position.getY() < column));
    }

    /**
     * Throw an excpetion if the position is not valid
     * @param position Point: position of selected ball
     */
    protected void isPositionException(Point position) {
        if (!isPositionCorrect(position)) {
            throw new IllegalArgumentException("La position n'est pas comprise dans le tableau");
        }
    }

    /**
     * Remove a ball from the balls board
     * @param position Point: position of selected ball
     */
    protected void removeBall(Point position) {
        balls[position.getX()][position.getY()] = null;
    }

    /**
     * Delete all ball that have neighbor and realign all the game
     * @return the number of delete number
     */
    public int deleteBall() {
        for (var i = 0; i < balls.length; i++) {
            for (var j = 0; j < balls[i].length; j++) {
                if (verifBalls[i][j]) {
                    removeBall(new Point(i, j));
                }
            }
        }
        realignBalls();
        return getNumberOfNeighbor(this);
    }

    /**
     * Check if the selected ball contain some neighbor 
     * @param positionCourante Point: position of selected ball
     */
    public void getNeighborBalls(Point positionCourante) {
        verifBalls = new boolean[line][column];
        char color = getColorBall(positionCourante);
        checkNeighborColor(positionCourante, color, this);
    }

    /**
     * Realign all the balls when it has been deleted.
     * Realign to the bottom and to the left when it is available
     */
    private void realignBalls() {
        for (var y = 0; y < balls[0].length; y++) {
            for (var x = balls.length - 1; x >= 0; x--) {
                movePosXBottom(x, y);
            }
            if (balls[balls.length - 1][y] != null && y > 0) {
                movePosY(balls.length - 1, y);
            }
        }
    }
    
    /**
     * Move a ball to the bottom such as it detect another ball
     * @param x int: position x of the ball
     * @param y int: position y of the ball
     */
    private void movePosXBottom(int x, int y) {
        if (x + 1 < balls.length && balls[x + 1][y] == null && balls[x][y] != null) {
            balls[x + 1][y] = balls[x][y];
            balls[x][y] = null;
            movePosXBottom(x + 1, y);
        }
    }

    /**
     * Facade method for the move ball to left 
     * @param x int: position x of the ball
     * @param y int: position y of the ball
     */
    private void movePosY(int x, int y) {
        movePosYLeft(x, y, 0, 0);
    }

    /**
     * Verif if the current ball have empty thin at his left.
     * If it is, it move all the ball to the left such as it find ball
     * @param x int: position x of the ball
     * @param y int: position y of the ball
     * @param numberColumnPassed int: number of column that have been passed
     * @param numberColumnNull int: number of finded column that was null
     */
    private void movePosYLeft(int x, int y, int numberColumnPassed, int numberColumnNull) {
        if (numberColumnPassed != y) {
            if (balls[x][numberColumnPassed] == null) { 
                ++numberColumnNull;
            }
            ++numberColumnPassed;
            movePosYLeft(x, y, numberColumnPassed, numberColumnNull);
        }
        if (balls[x][y] != null && numberColumnNull > 0) {
            movePosXLeft(balls.length - 1, y, numberColumnNull);
            
        }
    }

    /**
     * Move all the ball to the left
     * @param x int: position x of the ball
     * @param y int: position y of the ball
     * @param zeroLine int: nunmber of line that has been null
     */
    private void movePosXLeft(int x, int y, int zeroLine) {
        if (y - zeroLine >= 0 && x >= 0 && balls[x][y - zeroLine] == null) {
            balls[x][y - zeroLine] = balls[x][y];
            balls[x][y] = null;
            movePosXLeft(--x, y, zeroLine);
        }
    }

    /**
     * return if a ball is null or not
     * True if is not a ball else false
     * @param position
     * @return 
     */
    public boolean isBall(Point position) {
        return balls[position.getX()][position.getY()] != null;
    }

    /**
     * Return the balls
     * @return the balls
     */
    public Ball[][] getBalls() {
        return balls;
    }

    /**
     * Return a ball at a specified
     * @param position Point: position of a ball
     * @return a ball in the board
     */
    public Ball getBall(Point position) {
        return balls[position.getX()][position.getY()];
    }

    /**
     * Set true to a position in the neighbor array
     * @param position Point: position of boolean in array of verif
     */
    protected void setVerifBall(Point position) {
        verifBalls[position.getX()][position.getY()] = true;
    }

    /**
     * Get the neighbor verif ball
     * @return the neighbor verif ball
     */
    public boolean[][] getVerifBall() {
        return verifBalls;
    }

    /**
     * Return the color corresponding to the color.
     * If the ball is null it display an *
     * @param position point: position of the selected ball
     * @return the color of a ball or * for null
     */
    public char getColorBall(Point position) {
        if (balls[position.getX()][position.getY()] == null) {
            return '*';
        } else {
            return balls[position.getX()][position.getY()].getColor().toString().charAt(0);
        }
    }

    /**
     * Return the number of line
     * @return the number of line
     */
    public int getLine() {
        return line;
    }

    /**
     * Return the number of column
     * @return the number of column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set the board to a new one
     * @param balls balls[]: array of ball
     */
    public void setBoard(Ball[][] balls) {
        this.balls = balls;
    }

}
