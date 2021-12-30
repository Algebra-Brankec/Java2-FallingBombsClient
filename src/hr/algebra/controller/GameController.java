/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.udp.UnicastClientThread;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author brand
 */
public class GameController implements Initializable {

    private UnicastClientThread unicastCliThread;
    
    @FXML
    private Button btnFloor;
    @FXML
    private Button btnFence;
    @FXML
    private AnchorPane apLevel;
    @FXML
    private Button btnMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //unicastCliThread = new UnicastClientThread();
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
    
}
