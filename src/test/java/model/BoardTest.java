/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author basile
 */
public class BoardTest {
    
    Board board;
    
    public BoardTest() {
        
    }
    
    @BeforeEach
    public void setUp() {
        board = new Board();
    }
    
    @Test
    public void test_controller_specialize(){
        board = new Board(10, 12);
        assertEquals(10, board.getLine());
        assertEquals(12, board.getColumn());
    }

    @Test
    public void test_add_ball_check() {
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        assertEquals(ColorBall.BLUE, board.getBall(new Point(0,0)).getColor());
    }
    
    @Test
    public void test_add_ball_check_exception() {
        setUp();
        assertThrows(IllegalArgumentException.class,()-> board.addBall(new Point(-2,-2), ColorBall.BLUE));
    }
    
    @Test
    public void test_remove_ball(){
        setUp();
        board.removeBall(new Point(0,0));
        assertNull(board.getBall(new Point(0,0)));
    }
    
    @Test
    public void test_is_position_correct_false(){
        setUp();
        assertFalse(board.isPositionCorrect(new Point(-1,20)));
        assertFalse(board.isPositionCorrect(new Point(13,16)));
        assertFalse(board.isPositionCorrect(new Point(12,17)));
    }
    
    @Test
    public void test_is_position_correct_true(){
        setUp();
        assertTrue(board.isPositionCorrect(new Point(1,10)));
    }
    
    @Test
    public void test_is_position_exception_true(){
        setUp();
        assertThrows(IllegalArgumentException.class,()-> board.isPositionException(new Point(-1,3)));
    }
    
    @Test
    public void test_getNeighborBalls_ball_true(){
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        board.addBall(new Point(1,0), ColorBall.BLUE);
        board.addBall(new Point(0,1), ColorBall.BLUE);
        board.addBall(new Point(1,1), ColorBall.BLUE);
        board.getNeighborBalls(new Point(0,0));
        assertTrue(board.getVerifBall()[0][0]);
        assertTrue(board.getVerifBall()[1][0]);
        assertTrue(board.getVerifBall()[0][1]);
        assertTrue(board.getVerifBall()[1][1]);
    }
    
    @Test
    public void test_getNeighborBalls_ball_different(){
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        board.addBall(new Point(1,0), ColorBall.BLUE);
        board.addBall(new Point(0,1), ColorBall.BLUE);
        board.addBall(new Point(1,1), ColorBall.BLUE);
        
        board.addBall(new Point(2,2), ColorBall.RED);
        board.addBall(new Point(1,2), ColorBall.RED);
        board.addBall(new Point(2,1), ColorBall.RED);
        
        board.getNeighborBalls(new Point(0,0));
        
        assertTrue(board.getVerifBall()[0][0]);
        assertTrue(board.getVerifBall()[1][0]);
        assertTrue(board.getVerifBall()[0][1]);
        assertTrue(board.getVerifBall()[1][1]);
        assertFalse(board.getVerifBall()[2][2]);
        assertFalse(board.getVerifBall()[1][2]);
        assertFalse(board.getVerifBall()[2][1]);
    }
    
    @Test
    public void test_remove_ball_finded(){
        setUp();
        int line = board.getLine()-1;
        board.addBall(new Point(line-1,0), ColorBall.BLUE);
        board.addBall(new Point(line-1,1), ColorBall.BLUE);
        board.addBall(new Point(line-1,2 ), ColorBall.BLUE);
        
        board.addBall(new Point(line,0), ColorBall.RED);
        board.addBall(new Point(line,1), ColorBall.RED);
        board.addBall(new Point(line,2), ColorBall.RED);
        
        board.getNeighborBalls(new Point(line-1,0));
        board.deleteBall();
        
        assertNull(board.getBall(new Point(line-1,0)));
        assertNull(board.getBall(new Point(line-1,1)));
        assertNull(board.getBall(new Point(line-1,2)));
        
        
        assertNotNull(board.getBall(new Point(line,0)));
        assertNotNull(board.getBall(new Point(line,1)));
        assertNotNull(board.getBall(new Point(line,2)));
    }
    
    @Test
    public void test_realign_ball_after_delete(){
        setUp();
        int line = board.getLine()-1;
        board.addBall(new Point(0,0), ColorBall.BLUE);
        board.addBall(new Point(0,1), ColorBall.BLUE);
        board.addBall(new Point(0,2 ), ColorBall.BLUE);
        
        board.addBall(new Point(1,0), ColorBall.RED);
        board.addBall(new Point(1,1), ColorBall.RED);
        board.addBall(new Point(1,2), ColorBall.RED);
        
        board.getNeighborBalls(new Point(line-1,0));
        board.deleteBall();
        assertNull(board.getBall(new Point(0,0)));
        assertNull(board.getBall(new Point(0,1)));
        assertNull(board.getBall(new Point(0,2)));
        assertNull(board.getBall(new Point(1,0)));
        assertNull(board.getBall(new Point(1,1)));
        assertNull(board.getBall(new Point(1,2)));
        
        assertNotNull(board.getBall(new Point(line,0)));
        assertNotNull(board.getBall(new Point(line,1)));
        assertNotNull(board.getBall(new Point(line,2)));
    }

    @Test
    public void test_is_ball_true() {
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        
        assertTrue(board.isBall(new Point(0,0)));
        assertFalse(board.isBall(new Point(0,1)));
    }

    @Test
    public void test_get_ball() {
        setUp();
        assertNull(board.getBalls()[0][0]);
    }


    @Test
    public void test_get_color_ball_not_null() {
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        assertEquals('B', board.getColorBall(new Point(0,0)));
    }
    
    @Test
    public void test_get_color_ball_null() {
        setUp();
        assertEquals('*', board.getColorBall(new Point(0,0)));
    }

    @Test
    public void test_set_board() {
        setUp();
        board.addBall(new Point(0,0), ColorBall.BLUE);
        assertEquals('B', board.getColorBall(new Point(0,0)));
        
        board.setBoard(new Ball[board.getLine()][board.getColumn()]);
        assertNull(board.getBall(new Point(0,0)));
    }
    
}
