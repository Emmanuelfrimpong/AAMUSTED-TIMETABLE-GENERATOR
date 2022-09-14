package Controllers;

import GlobalFunctions.TableButtons;
import MongoServices.DatabaseServices;
import Objects.StudentsObject;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class StudentPageController implements Initializable {

    @FXML
    private TextField tf_search;
    @FXML
    private JFXButton btn_addNew;
    @FXML
    private JFXButton btn_import;
    @FXML
    private JFXButton btn_export;
    @FXML
    private TableView<StudentsObject> tb_studnts;
    @FXML
    private TableColumn<StudentsObject, String> col_level;
    @FXML
    private TableColumn<StudentsObject, String> col_name;
    @FXML
    private TableColumn<StudentsObject, String> col_department;
    @FXML
    private TableColumn<StudentsObject, String> col_size;
    @FXML
    private TableColumn<StudentsObject, String> col_disability;
    @FXML
    private TableColumn<StudentsObject, String> col_courses;
    @FXML
    private TableColumn<StudentsObject, Button> col_edit;
    @FXML
    private TableColumn<StudentsObject, Button> col_delete;

    /**
     * Initializes the controller class.
     */
    TableButtons TB = new TableButtons();
    private ObservableList<StudentsObject> data;
    private ObservableList<StudentsObject> selectectData;
    DatabaseServices services = new DatabaseServices();
    FileChooser fileChooser = new FileChooser();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getStudentsData();

    }

    void getStudentsData() {
        data = FXCollections.observableArrayList();
        Service<ObservableList<StudentsObject>> service = new Service<ObservableList<StudentsObject>>() {
            @Override
            protected Task<ObservableList<StudentsObject>> createTask() {
                return new Task<ObservableList<StudentsObject>>() {
                    @Override
                    protected ObservableList<StudentsObject> call() throws Exception {
                        return services.getAllStudents("AAMUSTED_DB");
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            data = service.getValue();            
            col_level.setCellValueFactory(new PropertyValueFactory<>("level"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            col_size.setCellValueFactory(new PropertyValueFactory<>("size"));
            col_disability.setCellValueFactory(new PropertyValueFactory<>("hasDisability"));
            col_courses.setCellValueFactory(new PropertyValueFactory<>("courses"));
            col_edit.setCellFactory(TB.addEditButton());
            col_delete.setCellFactory(TB.addDeleteButton());
            tb_studnts.setItems(null);
            tb_studnts.setItems(data);
            tb_studnts.getSelectionModel().clearSelection();
        });
        service.start();
    }

    @FXML
    private void handleImport(ActionEvent event) throws IOException {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile.exists()) {
            services.LoadStudentsToDatabase(stage, selectedFile, data);
        }
    }

}
