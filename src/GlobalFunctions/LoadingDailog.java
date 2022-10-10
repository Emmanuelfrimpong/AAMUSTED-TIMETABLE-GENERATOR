/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GlobalFunctions;

import Controllers.LoadingController;
import aamusted.timetable.generator.AAMUSTEDTIMETABLEGENERATOR;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author emman
 */
public class LoadingDailog {

    String message;

    public LoadingDailog(String message) {
        this.message = message;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    Stage stage = new Stage();

    public void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontEnds/Loading.fxml"));
            Parent parent = fxmlLoader.load();
            LoadingController controller = fxmlLoader.<LoadingController>getController();
            controller.setMessage(message);
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT); 
            stage.setScene(scene);
              stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AAMUSTEDTIMETABLEGENERATOR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void close(){
        if(stage.isShowing()){
           stage.close();
        }
    }

}
