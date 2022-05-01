/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * A point that have the X and Y position
 * @author g54018
 */
public class Point {

    private int x;
    private int y;

    /**
     * Constructor initiliaze the state all the attribut with the other constructor
     * @param p Point: the position of the point
     */
    public Point(Point p) {
        this(p.getX(), p.getY());
    }

    /**
     * Constructor initiliaze the state all the attribut
     * @param x Double: The X position
     * @param y Double: The Y position
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the position X attribut
     * @return the position of X attribut
     */
    public int getX() {
        return x;
    }

    /**
     * Return the position X attribut
     * @return the position of X attribut
     */
    public int getY() {
        return y;
    }
    
    /**
     * Move the X and Y position of the Point of the form
     * @param dx Double: The X position
     * @param dy Double: The Y position
     */
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }
    
}
