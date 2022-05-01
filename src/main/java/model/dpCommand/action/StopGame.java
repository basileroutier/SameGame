/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dpCommand.action;

import model.Game;
import model.dpCommand.Command;
import viewConsole.View;

/**
 *
 * @author basile
 */
public class StopGame implements Command{
    
    private View view;
    private Game game;
    
    public StopGame(Game game, View view) {
        this.game = game;
        this.view = view;
    }

    @Override
    public void execute() {
        game.gameFinish();
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isReversible() {
        return false;
    }

    @Override
    public boolean isHelp() {
        return false;
    }
    
}
