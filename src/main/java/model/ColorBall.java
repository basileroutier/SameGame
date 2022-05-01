/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package model;

/**
 * Enum of the color
 * @author basile
 */
public enum ColorBall {
    RED("#b02331"),
    GREEN("#3dad57"),
    BLUE("#1f4fad"),
    YELLOW("#bfb028"),
    ORANGE("#de8212"),
    
    ILL_RED("#d62f40"),
    ILL_GREEN("#4bd66b"),
    ILL_BLUE("#2a6beb"),
    ILL_YELLOW("#fce938"),
    ILL_ORANGE("#ed972d");

    public final String code;

    ColorBall(String code) {
        this.code = code;
    }
    
    public String getColor(){
        return this.code;
    }
}
