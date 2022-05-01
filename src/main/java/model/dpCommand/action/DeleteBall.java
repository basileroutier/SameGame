/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dpCommand.action;

import java.util.Arrays;
import model.Ball;
import model.Game;
import model.Point;
import model.dpCommand.Command;

/**
 *
 * @author basile
 */
public class DeleteBall implements Command {

    private Game game;
    private Point position;
    private Ball[][] originalBalls;
    private int lastScore;

    public DeleteBall(Game game, Point position) {
        this.game = game;
        this.position = position;
        
    }

    private void deepCopyBalls() {
        Ball[][] copyBalls = java.util.Arrays.stream(originalBalls).map(el -> el.clone()).toArray($ -> originalBalls.clone());
        originalBalls = copyBalls;
    }

    @Override
    public void execute() {
        this.lastScore = game.getScore();
        this.originalBalls = game.getBalls();
        deepCopyBalls();
        game.verifEndDeleteBall(position);
    }

    @Override
    public void unexecute() {
        game.changeBoard(originalBalls, lastScore);
    }

    @Override
    public boolean isReversible() {
        return true;
    }

    @Override
    public boolean isHelp() {
        return false;
    }
    
    public Point getPositionDelete(){
        return position;
    }

}
