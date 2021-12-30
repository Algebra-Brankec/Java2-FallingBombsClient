/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.controller.PlayerMenuController;
import hr.algebra.udp.MulticastClientThread;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author brand
 */
public class FallingBombsApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/Game.fxml"));
        
        Scene scene = new Scene(root);
        
        openPlayerMenu();
        
        startGame(scene);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void startGame(Scene scene){
        Thread gameThread = new Thread(() -> {
            PlayerMenuController playerMenuController = new PlayerMenuController();
            int serverPort = 0;
            while(!(serverPort > 0))
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FallingBombsApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
                serverPort = playerMenuController.getServerPort();
            }
            
            Game game = new Game(scene, serverPort);
            game.run();
        });  
        gameThread.setDaemon(true);
        gameThread.start();
    }
    
    private void openPlayerMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/PlayerMenu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("ABC");
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e){
        }
    }
    
}
