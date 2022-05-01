/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * State of the game State can change to know where are we in the game
 * Their is different state to know where we are in current game.
 * <ul>
 * <li>NOT_STARTED when game is not initialize</li>
 * <li>PICK_DELETE_BALL when the player can pick and delete a ball</li>
 * <li>CHECK_BALL_HOVER when the player hover on a button (in javaFX) to illuminate ball</li>
 * <li>GAME_END_OVER when the game is game over but can restart</li>
 * <li>GAME_FINISH when the game is finish</li>
 * </ul>
 * @author basile <54018@etu.he2b.be>
 */
public enum State {
    NOT_STARTED,
    PICK_DELETE_BALL,
    CHECK_BALL_HOVER,
    GAME_END_OVER,
    GAME_FINISH;
}
