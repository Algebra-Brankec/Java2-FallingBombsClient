/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author brand
 */
public class PlayerSetting {
    public PlayerSetting(String player1Color, String player2Color){
        this.player1Color = player1Color;
        this.player2Color = player2Color;
    }
    
    public String player1Color;
    public String player2Color;
}
