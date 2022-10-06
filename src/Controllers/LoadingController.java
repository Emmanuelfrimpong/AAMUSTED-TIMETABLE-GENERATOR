/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class LoadingController implements Initializable {

    @FXML
    private Label lb_message;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setMessage(String message) {
       if(message!=null){
           lb_message.setText(message);
       }else{
           lb_message.setText("Please wait....");
           
       }
    }
    
}
