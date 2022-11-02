package aamusted.timetable.generator;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import Objects.Configuration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author emman
 */
public class AAMUSTEDTIMETABLEGENERATOR extends Application {

    private static double xOffset = 0;
    private static double yOffset = 0;
    GlobalFunctions GF = new GlobalFunctions();
     Configuration config=new Configuration();
     DatabaseServices DBservices = new DatabaseServices();

    @Override
    public void start(Stage stage) {
        DBservices.createConfigurations();
         openLogin(stage);      
    }

    private void openLogin(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FrontEnds/LoginPage.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AAMUSTEDTIMETABLEGENERATOR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openSeetings(Stage stage) {

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnds/SettingsPage.fxml"));
            root = (Parent) loader.load();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setScene(scene);           
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(AAMUSTEDTIMETABLEGENERATOR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
