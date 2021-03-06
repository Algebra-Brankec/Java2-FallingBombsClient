/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class BtnPlayerHealth extends Button {
    private int widthChunk = 47;
    private int height = 25;
    private int maxChunkCount = 3;
    
    public BtnPlayerHealth(AnchorPane ap, int side, int health) {
        maxChunkCount = health;
        setPrefSize(widthChunk*maxChunkCount, height);
        setStyle("-fx-background-color: red");
        
        if (side == 1) {
            setLayoutX(28);
            setLayoutY(51);
        }
        if (side == 2) {
            setLayoutX(688);
            setLayoutY(51);
        }
        ap.getChildren().add(this);
    }
    
    public void updateSize(int value) {
        if (value >= maxChunkCount) {
            setPrefWidth(widthChunk*maxChunkCount);
            return;
        }
        
        if (value <= 0) {
            setPrefWidth(0);
            setVisible(false);
            return;
        }
        
        setPrefWidth(widthChunk*value);
    }
}
