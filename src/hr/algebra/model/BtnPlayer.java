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
    
    public BtnPlayer(AnchorPane ap, int side, int x, int y, int width, int height, int health, String colorLeft, String colorRight) {
        setPrefSize(width, height);
        setLayoutX(x);
        setLayoutY(y);
        switch(side){
            case 1:
                setStyle("-fx-background-color: " + colorLeft);
                this.side = "left";
                break;
            case 2:
                setStyle("-fx-background-color: " + colorRight); 
                this.side = "right";
                break;
        }
    }
}
