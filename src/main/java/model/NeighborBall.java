/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 * NeighBall contain all the static utils method for the neighbor of a ball.
 * It is use for get all the neighbor of ball, check if their is neighbor, ...
 * @author basile
 */
public class NeighborBall {

    
    /**
     * Fill the array of neighbor ball, if their is no neighbor it just set the
     * First one finded.
     * Continue to call the the isPosAdjacent method such as the condition is
     * Not correct
     * Check the neighbor at the left, right, bottom, top
     * @param positionCourante Point : Position of the selection ball
     * @param color char: the color of the selected ball
     * @param board Board: the board to modify the neighbor array
     */
    public static void checkNeighborColor(Point positionCourante, char color, Board board) {
        List<Point> voisins = List.of(
                new Point(positionCourante.getX() + 1, positionCourante.getY()),
                new Point(positionCourante.getX() - 1, positionCourante.getY()),
                new Point(positionCourante.getX(), positionCourante.getY() + 1),
                new Point(positionCourante.getX(), positionCourante.getY() - 1)
        );

        for (var voisinCourant : voisins) {
            if (isPosAdjacent(voisinCourant, color, board)) {
                board.setVerifBall(voisinCourant);
                checkNeighborColor(voisinCourant, color, board);
            }
        }
    }

    /**
     * Get the current position of the ball in the array and check if :
     * Is in array, is same color and if it not already verif.
     * If this condition is fill, it return true
     * Else return false 
     * @param positionCourante Point : Position of the selection ball
     * @param color char: the color of the selected ball
     * @param board Board: the board to modify the neighbor array
     * @return true if all the condition is respect else false
     */
    private static boolean isPosAdjacent(Point positionCourante, char color, Board board) {
        int x = positionCourante.getX();
        int y = positionCourante.getY();
        return (isInArray(x, y, board) && isSameColor(x, y, color, board) && isNotVerifYeat(x, y, board));
    }

    /**
     * Check if the selected ball is already verif or not.
     * If it is, it return false,
     * Else return true
     * @param x int: The X position of the ball
     * @param y int: The Y position of the ball
     * @param board Board: the board to modify the neighbor array
     * @return true if the ball not already verif else return false
     */
    private static boolean isNotVerifYeat(int x, int y, Board board) {
        return !board.getVerifBall()[x][y];
    }

    /**
     * Check if the selected ball is in array or not.
     * If it is, it return true
     * Else return false
     * @param x int: The X position of the ball
     * @param y int: The Y position of the ball
     * @param board Board: the board to modify the neighbor array
     * @return true if the ball is in array else false
     */
    private static boolean isInArray(int x, int y, Board board) {
        return ((x >= 0 && x < board.getBalls().length) && (y >= 0 && y < board.getBalls()[0].length));
    }

    /**
     * Check if the selected ball have the same color that the first one.
     * If it is, it return true
     * Else return false
     * @param x int: The X position of the ball
     * @param y int: The Y position of the ball
     * @param color char: the color of the first ball selected
     * @param board Board: the board to modify the neighbor array
     * @return true if it is the same color else false
     */
    private static boolean isSameColor(int x, int y, char color, Board board) {
        return board.getColorBall(new Point(x, y)) == color;
    }
    
    /**
     * Get all the neighbor that have been finded and
     * Return the number of neighbor finded that was true
     * @param board Board: the board to modify the neighbor array
     * @return the number of neighbor
     */
    public static int getNumberOfNeighbor(Board board){
        int numberOfNeighbor = 0;
        boolean verifBalls[][] = board.getVerifBall();
        for (var i = 0; i < verifBalls.length; i++) {
            for (var j = 0; j < verifBalls[i].length; j++) {
                if (verifBalls[i][j]==true) {
                    numberOfNeighbor++;
                }
            }
        }
        return numberOfNeighbor;
    }

    /**
     * Check if the number of neighbor is higher than 1
     * Return true if it is
     * Else false
     * @param board Board: the board to modify the neighbor array
     * @return true if the number of neighbor is higher than 1 else false
     */
    public static boolean isNeigbhorExist(Board board) {
        return getNumberOfNeighbor(board) > 1;
    }
}
