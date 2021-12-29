/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brand
 */
public class BtnBomb extends Button {
    public BtnBomb(int x, int y, int width, int height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: black");
        setLayoutX(x);
        setLayoutY(y);
    }
}
