/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package model;

import model.dpCommand.Command;
import model.dpCommand.action.DeleteBall;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author basile
 */
public class GameTest {
    
    private Game game;

    public GameTest() {
        //game = new Game();
    }

    public void setUp() {
        game = new Game();
    }
    
    public void initBoardSpecialColor(ColorBall colorChoice){
        for(var i=0;i<game.getLine();i++){
            for(var j=0;j<game.getColumn();j++){
                game.addBall(new Point(i,j), colorChoice);
            }
        }
    }
    
    @Test
    public void testbeginingGameWithoutStarting(){
        setUp();
        assertEquals(0, game.getBestScore());
        assertEquals(State.NOT_STARTED, game.getState());
    }

    @Test
    public void teststartingGame_defaultSize() {
        setUp();
        game.start(0);
        assertEquals(12, game.getLine());
        assertEquals(16, game.getColumn());
        assertEquals(4, game.getNumberColor());
        assertNotEquals(null, game.getBalls()[0][0]);
        assertEquals(State.PICK_DELETE_BALL, game.getState());
    }
    
    @Test
    public void teststartingGame_personnalizeSize() {
        setUp();
        game.start(15,15,3);
        assertEquals(15, game.getLine());
        assertEquals(15, game.getColumn());
        assertNotEquals(null, game.getBalls()[0][0]);
        assertEquals(State.PICK_DELETE_BALL, game.getState());
    }
    
    @Test
    public void teststartingGame_excpetionSizeLineColumn() {
        setUp();
        assertThrows(IllegalArgumentException.class,()-> game.start(30,30,6));
    }
    
     @Test
    public void teststartingGame_excpetionColor() {
        setUp();
        assertThrows(IllegalArgumentException.class,()-> game.start(10,12,6));
    }

    @Test
    public void teststateErrorStart() {
        setUp();
        game.start(0);
        assertThrows(IllegalStateException.class,()-> game.start(0));
    }

    @Test
    public void testpickBall_position_outside_board() {
        setUp();
        game.start(0);
        assertThrows(IllegalArgumentException.class, ()-> game.checkPickBall(new Point(-5,40)));
    }

    
    // Peut buguer car aléatoire
    // Un point n'a pas toujours des voisins donc peut soit être vrai ou faux
    @Test
    public void testpickBall_position_inside_board() {
        setUp();
        game.start(10,10,3);
        assertEquals(true, game.isBallHaveNeighbor(new Point(0,0)));
    }
    
    @Test
    public void testPickBall_error_state() {
        setUp();
        assertThrows(IllegalStateException.class, ()-> game.pickBall(new Point(1,1)));
    }
    
    // Peut buguer car aléatoire
    // Un point n'a pas toujours des voisins donc peut buguer
    @Test
    public void testPickBall_no_error() {
        setUp();
        game.start(10,10,3);
        game.pickBall(new Point(0,0));
        assertNotEquals(0, game.getScore());
        assertEquals(null, game.getBalls()[0][0]);
    }
    
    @Test
    public void testEndGame_error() {
        setUp();
        game.start(10,10,3);
        assertThrows(IllegalStateException.class, ()-> game.gameOver());
    }
    
    @Test
    public void testEndGame_false() {
        setUp();
        game.start(10,10,3);
        assertFalse(game.isWinGame());
    }
    
    
    @Test
    public void testLooseGame_false() {
        setUp();
        game.start(10,10,3);
        assertFalse(game.isLooseGame());
    }
    
    @Test
    public void test_check_pick_ball_no_neighbor_delete_ball(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.addBall(new Point(0,0), ColorBall.RED);
        assertThrows(IllegalArgumentException.class, ()-> game.pickBall(new Point(0,0))); 
    }
    
    
    @Test
    public void test_increment_score_when_delete_ball(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.addBall(new Point(0,0), ColorBall.RED);
        game.addBall(new Point(0,1), ColorBall.RED);
        game.pickBall(new Point(0,0));
        assertEquals(2, game.getScore());
    }
    
    @Test
    public void test_finish_bestScore(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.pickBall(new Point(0,0));
        assertNotEquals(0, game.getBestScore());
        assertEquals(State.GAME_END_OVER, game.getState());
    }
    
    @Test
    public void test_is_loose_when_delete_ball_true(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.addBall(new Point(0,0), ColorBall.RED);
        game.pickBall(new Point(1,0));
        assertTrue(game.isLooseGame());
    }
    
    @Test
    public void test_is_loose_when_delete_ball_false(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.addBall(new Point(0,0), ColorBall.RED);
        game.addBall(new Point(0,1), ColorBall.RED);
        game.pickBall(new Point(1,0));
        assertFalse(game.isLooseGame());
    }
    
    @Test
    public void test_is_win_when_delete_ball_true(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.pickBall(new Point(0,0));
        assertTrue(game.isWinGame());
    }
    
    @Test
    public void test_is_win_when_delete_ball_false(){
        setUp();
        game.start(0);
        initBoardSpecialColor(ColorBall.BLUE);
        game.addBall(new Point(0,0), ColorBall.RED);
        game.pickBall(new Point(1,0));
        assertFalse(game.isWinGame());
    }
    
    @Test
    public void test_change_board(){
        setUp();
        game.start(0);
        game.changeBoard(new Ball[10][10], 0);
        assertEquals(0, game.getScore());
        assertEquals(null, game.getBalls()[0][0]);
    }
    
    @Test
    public void test_remain_ball(){
        setUp();
        game.start(0);
        game.changeBoard(new Ball[10][10], 0);
        game.addBall(new Point(0,0), ColorBall.RED);
        assertEquals(1, game.getNumberRemainBall());
    }
    
    
}
