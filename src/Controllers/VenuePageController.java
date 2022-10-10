package Controllers;

import GlobalFunctions.ActionButtonTableCell;
import GlobalFunctions.ExcelServices;
import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import MongoServices.DatabaseServices;
import Objects.Venue;
import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class VenuePageController implements Initializable {

    @FXML
    private TextField tf_search;
    @FXML
    private JFXButton btn_import;
    @FXML
    private ImageView btn_refresh;
    @FXML
    private TableView<Venue> tb_venue;
    @FXML
    private TableColumn<Venue, String> col_name;
    @FXML
    private TableColumn<Venue, String> col_capacity;
    @FXML
    private TableColumn<Venue, String> col_disability;
    @FXML
    private TableColumn<Venue, Button> col_edit;
    @FXML
    private TableColumn<Venue, Button> col_delete;

    DatabaseServices DBservices = new DatabaseServices();
    FileChooser fileChooser = new FileChooser();
    GlobalFunctions GF = new GlobalFunctions();
    private ObservableList<Venue> venueData;
      private ExcelServices ES = new ExcelServices();
    @FXML
    private JFXButton btn_download;

  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getVenues();
    }

    @FXML
    private void handleImport(ActionEvent event) {
          fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile!=null&&selectedFile.exists()) {                   
              ObservableList<Venue> incomingData = DBservices.LoadVenue(selectedFile);
              if(incomingData!=null){
                //DBservices.createVenueTimePair();  
                getVenues();
              }
              GF.showToast("Venue Saved Successfully", stage);                      
        }
    }
    
    
    private void getVenues(){
         venueData = FXCollections.observableArrayList();     
        Service< ObservableList<Venue>> service = new Service< ObservableList<Venue>>() {
            @Override
            protected Task< ObservableList<Venue>> createTask() {
                return new Task<ObservableList<Venue>>() {
                    @Override
                    protected ObservableList<Venue> call() throws Exception {
                        return DBservices.getVenues();
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            venueData = service.getValue();
            Stage stage = (Stage) tb_venue.getScene().getWindow();
        String deletePathe = "/images/delete.png";
        String editPathe = "/images/edit.png";
        String deleteStyle = "/Styles/delete_button_style.css";
        String editStyle = "/Styles/update_button_style.css";

        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        col_disability.setCellValueFactory(new PropertyValueFactory<>("isDisabilityAccessible"));
        
        col_delete.setCellFactory(ActionButtonTableCell.<Venue>forTableColumn(deletePathe, deleteStyle, (Venue venue) -> {
            ButtonType submit = new ButtonType("Dlete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = GF.showAlert("Are you sure you want to delete this Venue Class?", submit, cancel).showAndWait();
            if (result.orElse(cancel) == submit) {
                if (DBservices.deleteData("Venue", venue.get_id()) != null) {
                    getVenues();
                    GF.showToast("Venue deleted Succefully", stage);
                } else {
                    GF.showToast("Faild to delete Venue", stage);
                }
            }

            return venue;
        }));
        col_edit.setCellFactory(ActionButtonTableCell.<Venue>forTableColumn(editPathe, editStyle, (Venue venue) -> {
            return venue;
        }));
        tb_venue.setItems(null);
        tb_venue.setItems(venueData);
        tb_venue.getSelectionModel().clearSelection();
            

        });
        service.start();
    }

   


    @FXML
    private void handleRfresh(MouseEvent event) {
        getVenues();
    }

    @FXML
    private void handleDownload(ActionEvent event) {
        
        ES.ExportVenue();
       
    
                }

}
