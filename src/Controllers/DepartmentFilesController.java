package Controllers;

import GlobalFunctions.ActionButtonTableCell;
import GlobalFunctions.ExcelServices;
import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import MongoServices.DatabaseServices;
import Objects.CoursesObject;
import Objects.StudentsObject;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class DepartmentFilesController implements Initializable {

    @FXML
    private TextField tf_search;
    @FXML
    private JFXButton btn_addNew;
    @FXML
    private TableView<StudentsObject> tb_studnts;
    @FXML
    private TableColumn<StudentsObject, String> col_level;

    @FXML
    private TableColumn<StudentsObject, String> col_department;
    @FXML
    private TableColumn<StudentsObject, String> col_size;
    @FXML
    private TableColumn<StudentsObject, String> col_disability;
    @FXML
    private TableColumn<StudentsObject, String> col_courses;
    @FXML
    private TableColumn<StudentsObject, String> col_className;
    @FXML
    private TableColumn<StudentsObject, Button> col_edit;
    @FXML
    private TableColumn<StudentsObject, Button> col_delete;
    @FXML
    private JFXButton btn_import;
    @FXML
    private ImageView btn_refresh;
    @FXML
    private TableView<CoursesObject> tb_courses;
    @FXML
    private TableColumn<CoursesObject, String> col_code;
    @FXML
    private TableColumn<CoursesObject, String> col_title;
    @FXML
    private TableColumn<CoursesObject, String> col_hours;
    @FXML
    private TableColumn<CoursesObject, String> col_venue;
    @FXML
    private TableColumn<?, ?> col_lecturer;

    @FXML
    private TableColumn<CoursesObject, String> col_email;
    @FXML
    private TableColumn<CoursesObject, String> col_phone;
    @FXML
    private TableColumn<CoursesObject, Button> col_editCourse;
    @FXML
    private TableColumn<CoursesObject, Button> col_deleteCours;

    private ObservableList<StudentsObject> studentsData;
    private ObservableList<CoursesObject> coursesData;

    DatabaseServices DBservices = new DatabaseServices();
    FileChooser fileChooser = new FileChooser();
    ObservableMap<String, Object> data;
    GlobalFunctions GF = new GlobalFunctions();
    @FXML
    private TableColumn<CoursesObject, String> col_lectName;
    @FXML
    private JFXButton btn_download;

    private ExcelServices ES = new ExcelServices();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getDepartmentData();
    }

    @FXML
    private void handleImport(ActionEvent event) throws IOException {

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.exists()) {
            try {

                ObservableMap<String, Object> incomingData = DBservices.LoadStudentsToDatabase(selectedFile);
                if ((boolean) incomingData.get("isSaved")) {

                    getDepartmentData();
                    GF.showToast("Data saved Succefully", stage);
                }

            } catch (IOException ex) {

                GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);

            }
        }
    }

    @FXML
    private void handleAddNew(ActionEvent event) {
    }

    @FXML
    private void handleRfresh(MouseEvent event) {
        getDepartmentData();
    }

    void getDepartmentData() {
        LoadingDailog loading = new LoadingDailog("Getting Data........");
        data = FXCollections.observableHashMap();
        studentsData = FXCollections.observableArrayList();
        coursesData = FXCollections.observableArrayList();
        Service< ObservableMap<String, Object>> service = new Service< ObservableMap<String, Object>>() {
            @Override
            protected Task< ObservableMap<String, Object>> createTask() {
                return new Task< ObservableMap<String, Object>>() {
                    @Override
                    protected ObservableMap<String, Object> call() throws Exception {
                        return DBservices.getDepartmentData("AAMUSTED_DB");
                    }
                };
            }
        };
        service.setOnFailed((WorkerStateEvent event) -> {
            loading.close();
           
        });
        service.setOnRunning((WorkerStateEvent event) -> loading.show());
        service.setOnSucceeded((WorkerStateEvent event) -> {
            loading.close();
            data = service.getValue();
            studentsData = (ObservableList<StudentsObject>) data.get("classes");
            coursesData = (ObservableList<CoursesObject>) data.get("courses");
            Stage stage = (Stage) tb_studnts.getScene().getWindow();
            LoadStudents(stage, studentsData);
            LoadCourses(stage, coursesData);

        });
        service.start();
    }

    private void LoadStudents(Stage stage, ObservableList<StudentsObject> studentData) {

        String deletePathe = "/images/delete.png";
        String editPathe = "/images/edit.png";
        String deleteStyle = "/Styles/delete_button_style.css";
        String editStyle = "/Styles/update_button_style.css";

        col_level.setCellValueFactory(new PropertyValueFactory<>("level"));
        col_className.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        col_size.setCellValueFactory(new PropertyValueFactory<>("size"));
        col_disability.setCellValueFactory(new PropertyValueFactory<>("hasDisability"));
        col_courses.setCellValueFactory(new PropertyValueFactory<>("courses"));
        col_delete.setCellFactory(ActionButtonTableCell.<StudentsObject>forTableColumn(deletePathe, deleteStyle, (StudentsObject student) -> {
            ButtonType submit = new ButtonType("Dlete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = GF.showAlert("Are you sure you want to delete this Student Class?", submit, cancel).showAndWait();
            if (result.orElse(cancel) == submit) {
                if (DBservices.deleteData("Student_Class", student.get_id()) != null) {
                    getDepartmentData();
                    GF.showToast("Sudent Class deleted Succefully", stage);
                } else {
                    GF.showToast("Faild to delete Student Class", stage);
                }
            }
            return student;
        }));
        col_edit.setCellFactory(ActionButtonTableCell.<StudentsObject>forTableColumn(editPathe, editStyle, (StudentsObject student) -> {
            return student;
        }));
        tb_studnts.setItems(null);
        tb_studnts.setItems(studentData);
        tb_studnts.getSelectionModel().clearSelection();
    }

    private void LoadCourses(Stage stage, ObservableList<CoursesObject> courseData) {

        String deletePathe = "/images/delete.png";
        String editPathe = "/images/edit.png";
        String deleteStyle = "/Styles/delete_button_style.css";
        String editStyle = "/Styles/update_button_style.css";
        col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_hours.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        col_venue.setCellValueFactory(new PropertyValueFactory<>("specialVenue"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("lecturerPhone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("lecturerEmail"));
        col_lectName.setCellValueFactory(new PropertyValueFactory<>("lecturerName"));
        col_deleteCours.setCellFactory(ActionButtonTableCell.<CoursesObject>forTableColumn(deletePathe, deleteStyle, (CoursesObject course) -> {
            ButtonType submit = new ButtonType("Dlete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = GF.showAlert("Are you sure you want to delete this Course from list?", submit, cancel).showAndWait();
            if (result.orElse(cancel) == submit) {
                if (DBservices.deleteData("Courses", course.getId()) != null) {
                    getDepartmentData();
                    GF.showToast("Course  deleted Succefully", stage);
                } else {
                    GF.showToast("Faild to delete Course", stage);
                }
            }
            return course;
        }));
        col_editCourse.setCellFactory(ActionButtonTableCell.<CoursesObject>forTableColumn(editPathe, editStyle, (CoursesObject course) -> {
            return course;
        }));
        tb_courses.setItems(null);
        tb_courses.setItems(courseData);
        tb_courses.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDownload(ActionEvent event) {
        List<String> departments = new ArrayList();
        departments.add("For Department");
        departments.add("For Libral/African Studies");
        ButtonType submit = new ButtonType("Submit Data", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Select Source of file and proceed");
        DialogPane dialogPane = dialog.getDialogPane();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialogPane.getStylesheets().add("/Styles/dialogStyle.css");
        dialogPane.getButtonTypes().addAll(submit, cancel);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(departments);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, comboBox));
        dialog.setResultConverter((ButtonType button) -> {
            if (button == submit) {
                return comboBox.getValue();
            }
            return null;
        });

        Optional<String> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((String results) -> {
            ES.ExportDepartment(results);
        });
    }

}
