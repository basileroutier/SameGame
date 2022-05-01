/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dpCommand;

/**
 * Command interface that have all method for the undo/redo command
 * @author basile
 */
public interface Command {
    /**
     * Executes the command
     */
    public void execute();
    
    /**
     * Unexecutes the command passed
     */
    public void unexecute();
    
    /**
     * Return true if a command can be undo
     * Else return false
     * @return true if  a command can be undo else false
     */
    public boolean isReversible();
    
    /**
     * Return if it is help
     * @return if the command is help
     */
    public boolean isHelp();
}
