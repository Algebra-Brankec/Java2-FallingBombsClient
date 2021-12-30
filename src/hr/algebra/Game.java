/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.BtnBomb;
import hr.algebra.model.Bomb;
import hr.algebra.model.BtnPlayer;
import hr.algebra.model.BtnPlayerHealth;
import hr.algebra.model.Player;
import hr.algebra.model.UDPDataPackage;
import hr.algebra.udp.MulticastClientThread;
import hr.algebra.udp.UnicastClientThread;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brand
 */
public class Game {
    private MulticastClientThread t1;
    private UnicastClientThread unicastCliThread1;
    private int serverPort;
    
    private UDPDataPackage udpPackage = new UDPDataPackage();
    
    private final Scene scene;
    private final AnchorPane apLevel;
    
    private List<BtnBomb> btnBombs;
    private List<BtnPlayer> btnPlayers;
    private List<BtnPlayerHealth> btnPlayerHealths;
    
    private boolean gameOver = false;
    
    public Game(Scene scene, int serverPort) {
        this.scene = scene;
        this.serverPort = serverPort;
        apLevel = (AnchorPane) scene.lookup("#apLevel");
        
        btnBombs = new ArrayList<>();
        btnPlayers = new ArrayList<>();
        btnPlayerHealths = new ArrayList<>();
    }
    
    public void run() {
        startUDCSockets();
        inputControl();
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

                //FX run on a specific thread that this will wait for it
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        udpPackage = t1.getUdpPackage();
                        clearScreen();
                        renderBombs();
                        renderPlayers();
                        
                        gameOverMessage();
                    }
                });
            }
        });  
        gameThread.setDaemon(true);
        gameThread.start();
    }
    
    private void inputControl() {
        scene.setOnKeyPressed(ke -> {
            HashSet<String> key = new HashSet<>();
            key.add(ke.getCode().toString());
                
            if (key.contains("A")){
                unicastCliThread1.setPlayerMovement(1);
            }
            
            if (key.contains("D")){
                unicastCliThread1.setPlayerMovement(2);
            } 
        });
        
        scene.setOnKeyReleased(ke -> {
            HashSet<String> key = new HashSet<>();
            key.add(ke.getCode().toString());
                
            if (key.size() > 0){
                unicastCliThread1.setPlayerMovement(0);
            }
        });
    }
    
    public void setUDPDataPackage(UDPDataPackage udpPackage) {
        this.udpPackage = udpPackage;
    }
    
    private void renderBombs() {
        for (Bomb bomb : udpPackage.getBombs()) {
            BtnBomb btnBomb = new BtnBomb(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight());
            btnBombs.add(btnBomb);
            
            apLevel.getChildren().add(btnBomb);
        }
    }
    
    private void renderPlayers() {
        for (Player player : udpPackage.getPlayers()) {
            BtnPlayer btnPlayer = new BtnPlayer(apLevel, player.getSide(), player.getX(), player.getY(), player.getWidth(), player.getHeight(), player.getHealth());
            BtnPlayerHealth btnPlayerHealth = new BtnPlayerHealth(apLevel, player.getSide(), player.getHealth());
            btnPlayers.add(btnPlayer);
            btnPlayerHealths.add(btnPlayerHealth);
            
            apLevel.getChildren().add(btnPlayer);
        }
    }

    private void clearScreen() {
        for(BtnBomb btnBomb : btnBombs) {
            apLevel.getChildren().remove(btnBomb);
        }
        btnBombs.clear();
        
        for(BtnPlayer btnPlayer : btnPlayers) {
            apLevel.getChildren().remove(btnPlayer);
        }
        btnPlayers.clear();
        
        for(BtnPlayerHealth btnPlayerHealth : btnPlayerHealths) {
            apLevel.getChildren().remove(btnPlayerHealth);
        }
        btnPlayerHealths.clear();
    }

    private void startUDCSockets() {
        t1 = new MulticastClientThread();
        t1.setDaemon(true);
        t1.start();
        
        unicastCliThread1 = new UnicastClientThread("localhost", serverPort);
        unicastCliThread1.setDaemon(true);
        unicastCliThread1.start();
    }
    
        
    private int getLastPlayerIndex(){
        for (int i = 0; i < udpPackage.getPlayers().size(); i++) {
            if (udpPackage.getPlayers().get(i).getHealth() > 0) {
                return i;
            }
        }
        
        return 0;
    }
    
    private void gameOverMessage(){
        if (gameOver){
            return;
        }
        
        int playerIndex = getLastPlayerIndex();
        
        if(playerIndex < 1){
            return;
        }
        
        gameOver = true;
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("Game over!");
        alert.setContentText("Player " + playerIndex + " won!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                Platform.exit();
            }
        });
    }
}
