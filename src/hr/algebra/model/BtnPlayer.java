/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brand
 */
public class BtnPlayer extends Button {
    public String side;
    public int maxHealth = 3;
    public int health = 3;
    public boolean stunned = false;
    public int speed = 20;
    
    private BtnPlayerHealth btnPlayerHealth;
    
    public BtnPlayer(AnchorPane ap, int side, int x, int y, int width, int height) {
        setPrefSize(width, height);
        setLayoutX(x);
        setLayoutY(y);
        switch(side){
            case 1:
                setStyle("-fx-background-color: green");
                this.side = "left";
                break;
            case 2:
                setStyle("-fx-background-color: red"); 
                this.side = "right";
                break;
        }
        
        btnPlayerHealth = new BtnPlayerHealth(ap, this.side);
    }
    
    public void takeDamage(int value) {
        if (health - value < 0) {
            die();
            return;
        }
        
        if (health - value >= 0) {
            health -= value;
            btnPlayerHealth.updateSize(health);
        }
        
        if (health <= 0) {
            die();
        }
    }
    
    public void getHealth(int value) {
        if (health <= 0) {
            die();
        }
        
        if (health + value > maxHealth) {
            health = maxHealth;
            return;
        }
        
        health += value;
        btnPlayerHealth.updateSize(health);
    }
    
    private void die() {
        stunned = true;
        speed = 0;
        health = 0;
        btnPlayerHealth.updateSize(health);
    }
}
