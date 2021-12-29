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
import hr.algebra.udp.MulticastClientThread;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brand
 */
public class Game {
    private MulticastClientThread t1;
    
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
        startUDCSockets();
        gameLoop();
    }
    
    private void gameLoop() {
        Thread gameThread = new Thread(() -> {
            Calendar cal = Calendar.getInstance();
            int now = (int) cal.getTimeInMillis();
            int lastFrame = (int) cal.getTimeInMillis();

            while(true)
            {
                //limiting the while loop to 30 times a second
                now = (int) cal.getTimeInMillis();
                int delta = now - lastFrame;
                lastFrame = now;

                if(delta < 33)
                {
                    try {
                        Thread.sleep(33 - delta);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        udpPackage = t1.getUdpPackage();
                        ClearScreen();
                        RenderBombs();
                    }
                });
            }
        });  
        gameThread.setDaemon(true);
        gameThread.start();
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

    private void startUDCSockets() {
        t1 = new MulticastClientThread();
        t1.setDaemon(true);
        t1.start();
    }
}
