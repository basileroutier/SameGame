/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package samegame;

import controllerConsole.Controller;
import model.Game;
import viewConsole.View;

/**
 *
 * @author basile
 */
public class main {
    public static void main(String[] args) {
        Game game = new Game();
        View view = new View(game);
        Controller controller = new Controller(view, game);
        controller.play();
    }
}
