/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.BtnBomb;
import hr.algebra.model.Bomb;
import hr.algebra.model.Player;
import hr.algebra.model.UDPDataPackage;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brand
 */
public class Game {
    private UDPDataPackage udpPackage = new UDPDataPackage();
    private Player player;
    
    private final Scene scene;
    private final AnchorPane apLevel;
    
    private List<BtnBomb> btnBombs;
    
    public Game(Scene scene) {
        this.scene = scene;
        apLevel = (AnchorPane) scene.lookup("#apLevel");
        
        player = new Player();
        
        btnBombs = new ArrayList<>();
    }
    
    public void run() {
        gameLoop();
    }
    
    private void gameLoop() {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                ClearScreen();

                RenderBombs();
            }
        });
    }
    
    public void setUDPDataPackage(UDPDataPackage udpPackage) {
        this.udpPackage = udpPackage;
    }
    
    private void RenderBombs() {

        for (Bomb bomb : udpPackage.getBombs()) {
            BtnBomb btnBomb = new BtnBomb(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight());
            btnBombs.add(btnBomb);
            
            apLevel.getChildren().add(btnBomb);
        }
    }

    private void ClearScreen() {
        for(BtnBomb btnBomb : btnBombs) {
            apLevel.getChildren().remove(btnBomb);
        }
        
        btnBombs.clear();
    }
}
