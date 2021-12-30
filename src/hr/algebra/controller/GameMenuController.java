/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.udp.UnicastClientThread;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author brand
 */
public class GameMenuController implements Initializable {
    
    private UnicastClientThread unicastCliThread1;

    @FXML
    private Button btnSaveGame;
    @FXML
    private Button btnLoadGame;
    @FXML
    private Button btnSaveGameDoc;
    @FXML
    private Button btnResume;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnSaveGameAction(ActionEvent event) {
        int playerAction = unicastCliThread1.getPlayerAction();
        if(playerAction != 102)
            unicastCliThread1.setPlayerAction(102);
        else {
            //resetting it to 0 then waiting 0.5 seconds until sending 102 again
            //because the client won't send the same code more than once in a row
            unicastCliThread1.setPlayerAction(0);
            delay(500);
            unicastCliThread1.setPlayerAction(102);
        }
    }

    @FXML
    private void btnLoadGameAction(ActionEvent event) {
    }

    @FXML
    private void btnSaveGameDocAction(ActionEvent event) {
    }

    @FXML
    private void btnResumeAction(ActionEvent event) {
    }
    
    private void delay(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
