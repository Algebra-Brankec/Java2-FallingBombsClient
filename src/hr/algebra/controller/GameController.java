/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.model.Bomb;
import hr.algebra.model.BtnBomb;
import hr.algebra.model.BtnPlayer;
import hr.algebra.model.BtnPlayerHealth;
import hr.algebra.model.Player;
import hr.algebra.model.UDPDataPackage;
import hr.algebra.udp.MulticastClientThread;
import hr.algebra.udp.UnicastClientThread;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author brand
 */
public class GameController implements Initializable {

    private MulticastClientThread t1;
    private UnicastClientThread unicastCliThread;
    private int serverPort;
    private PlayerMenuController playerMenuController;
    
    private UDPDataPackage udpPackage = new UDPDataPackage();
    
    private List<BtnBomb> btnBombs;
    private List<BtnPlayer> btnPlayers;
    private List<BtnPlayerHealth> btnPlayerHealths;
    
    private boolean running = false;
    private boolean gameOver = false;
    private boolean gameOverMessage = false;
    
    private boolean gameStart = false;
    
    @FXML
    private Button btnFloor;
    @FXML
    private Button btnFence;
    @FXML
    private AnchorPane apLevel;
    @FXML
    private Button btnMenu;
    @FXML
    private Button btnPauseResume;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerMenuController = new PlayerMenuController();
        
        btnBombs = new ArrayList<>();
        btnPlayers = new ArrayList<>();
        btnPlayerHealths = new ArrayList<>();
    }    

    @FXML
    private void keyPressed(KeyEvent event) {
    }

    @FXML
    private void keyReleased(KeyEvent event) {
    }

    @FXML
    private void btnMenuAction(ActionEvent event) {
        //int playerAction = unicastCliThread.getPlayerAction();
        //
        //if(playerAction != 101)
        //    unicastCliThread.setPlayerAction(101);
        //else {
        //    //Sending singla to resume the game then sending 0 as the default state
        //    unicastCliThread.setPlayerAction(100);
        //}
    }
    
    @FXML
    private void btnPauseResumeAction(ActionEvent event) {
        if (!gameStart) {
           run(); 
           gameStart = true;
           running = true;
           btnPauseResume.setText("Pause"); 
           
           return;
        }
        
        if(running){
           btnPauseResume.setText("Resume");
           unicastCliThread.setPlayerAction(101);
           running = false;
        } else {
           btnPauseResume.setText("Pause");  
           unicastCliThread.setPlayerAction(100);
           running = true;
        }
    }
    
    public void run() {
        startUDCSockets();
        inputControl();
        gameLoop();
    }
    
    private void gameLoop() {
        Thread gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double ns = 1000000000.0 / 30.0;
            double delta = 0;
            while(true){
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while(delta >= 1){
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
                    delta--;
                }
            }
        });  
        gameThread.setDaemon(true);
        gameThread.start();
    }
    
    private void inputControl() {
        apLevel.getScene().setOnKeyPressed(ke -> {
            HashSet<String> key = new HashSet<>();
            key.add(ke.getCode().toString());
            int playerAction = unicastCliThread.getPlayerAction();
            
            //suspend further actions until action is at number 100(Game Resumed)
            if(playerAction == 101){
                return;
            }
            
            //Anything below this line cannot be executed while the game is paused
            if (key.contains("A")){
                unicastCliThread.setPlayerAction(1);
            }
            
            if (key.contains("D")){
                unicastCliThread.setPlayerAction(2);
            } 
        });
        
        apLevel.getScene().setOnKeyReleased(ke -> {
            HashSet<String> key = new HashSet<>();
            key.add(ke.getCode().toString());
            int playerAction = unicastCliThread.getPlayerAction();
            
            if (key.size() > 0 && (playerAction == 1 || playerAction == 2)){
                unicastCliThread.setPlayerAction(0);
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
        
        unicastCliThread = new UnicastClientThread("localhost", playerMenuController.getServerPort());
        unicastCliThread.setDaemon(true);
        unicastCliThread.start();
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
        if (!gameOver){
            return;
        }
        
        if (gameOverMessage){
            return;
        }
        
        int playerIndex = getLastPlayerIndex() + 1;
        
        if(playerIndex < 1){
            return;
        }
        
        gameOverMessage = true;
        
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
