/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * A ball that conntain a position and a color
 * @author basile
 */
public class Ball {
    private Point position;
    private ColorBall color;

    /**
     * Simple constructor with check exception for position and color
     * @param position Point : position of selected ball
     * @param color char: color of a ball
     */
    public Ball(Point position, ColorBall color) {
        if(position==null){
            throw new IllegalArgumentException("La position ne peut pas être null");
        }
        if(color==null){
            throw new IllegalArgumentException("La couleur ne peut pas être null");
        }
        this.position = position;
        this.color = color;
    }

    /**
     * Return the position of the ball
     * @return the position of the ball
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Return the color of the ball
     * @return the color of the ball
     */
    public ColorBall getColor() {
        return color;
    }

    /**
     * Set a new position to the ball
     * @param position point: new position of the ball
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Set a new Color to the ball
     * @param color char: color of a ball
     */
    public void setColor(ColorBall color) {
        this.color = color;
    }
    
}
