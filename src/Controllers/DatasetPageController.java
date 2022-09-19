package Controllers;

import GlobalFunctions.GlobalFunctions;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;


public class DatasetPageController implements Initializable {


    @FXML
    private JFXButton btn_venue;

    @FXML
    private StackPane main_container;
   
    @FXML
    private JFXButton btn_department;
    @FXML
    private JFXButton btn_import;
    
     String selectedPage = "Students";
    GlobalFunctions GF = new GlobalFunctions();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_department.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
        GF.sideBarClick(main_container, "/FrontEnds/departmentFiles.fxml");
    }

 

    @FXML
    private void switchVenue(ActionEvent event) {
        if (!"Venue".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/VenuePage.fxml");
            btn_venue.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_department.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
           
            selectedPage = "Venue";
        }
    }

 

    @FXML
    private void handleImport(ActionEvent event) {
    }

    @FXML
    private void switchDepartment(ActionEvent event) {
         if (!"Department".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/departmentFiles.fxml");
            btn_department.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_venue.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
           
            selectedPage = "Department";
        }
    }

}
