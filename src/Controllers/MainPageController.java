package Controllers;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class MainPageController implements Initializable {

    @FXML
    private ImageView bt_minimize;
    @FXML
    private ImageView bt_close;

    @FXML
    private VBox menu_data;
    @FXML
    private VBox menu_tables;
    @FXML
    private VBox menu_settings;
    @FXML
    private VBox menu_signout;
    @FXML
    private Label lb_time;

    String selectedPage = "Data Set";
    @FXML
    private StackPane center_container;

    GlobalFunctions GF = new GlobalFunctions();
    DatabaseServices DBservices = new DatabaseServices();

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        GF.getTime(lb_time);
        GF.sideBarClick(center_container, "/FrontEnds/DatasetPage.fxml");
        menu_data.setStyle("-fx-background-color: #AC637C");
        bt_close.setOnMouseClicked(GF::closeWindow);

        //here we handle the minimize  button
        bt_minimize.setOnMouseClicked((Event event) -> {
            Stage thisStage = (Stage) bt_minimize.getScene().getWindow();
            thisStage.setIconified(true);
        });
         
    }

//   
    @FXML
    private void switchToDataset(MouseEvent event) {
        if (!"Data Set".equals(selectedPage)) {
            GF.sideBarClick(center_container, "/FrontEnds/DatasetPage.fxml");
            menu_data.setStyle("-fx-background-color: #AC637C");
            menu_settings.setStyle("-fx-background-color: #f2f2f2");
            menu_tables.setStyle("-fx-background-color: #f2f2f2");
            selectedPage = "Data Set";
        }
    }

    @FXML
    private void switchToTables(MouseEvent event) {
        if (!"Tables".equals(selectedPage)) {
            GF.sideBarClick(center_container, "/FrontEnds/TablesPage.fxml");
            menu_data.setStyle("-fx-background-color: #f2f2f2");
            menu_settings.setStyle("-fx-background-color: #f2f2f2");
            menu_tables.setStyle("-fx-background-color: #AC637C");
            selectedPage = "Tables";
        }
    }

    @FXML
    private void switchToSettings(MouseEvent event) {
        if (!"Settings".equals(selectedPage)) {
            GF.sideBarClick(center_container, "/FrontEnds/SettingsPage.fxml");
            menu_data.setStyle("-fx-background-color: #f2f2f2");
            menu_settings.setStyle("-fx-background-color: #AC637C");
            menu_tables.setStyle("-fx-background-color: #f2f2f2");
            selectedPage = "Settings";
        }
    }

    @FXML
    private void logUserOut(MouseEvent event) {
    }



}
