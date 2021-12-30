/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author brand
 */
public class PlayerMenuController implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btnPlayer1;
    @FXML
    private Button btnPlayer2;
    
    private static int serverPort;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onBtnPlayer1Action(ActionEvent event) {
        serverPort = 34530;
    }

    @FXML
    private void onBtnPlayer2Action(ActionEvent event) {
        serverPort = 34531;
    }

    public int getServerPort() {
        return serverPort;
    }
    
}
