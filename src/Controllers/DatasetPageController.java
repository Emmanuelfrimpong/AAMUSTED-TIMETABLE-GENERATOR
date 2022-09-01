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
    private JFXButton btn_student;
    @FXML
    private JFXButton btn_venue;
    @FXML
    private JFXButton btn_course;
    @FXML
    private JFXButton btn_lecturer;
    @FXML
    private StackPane main_container;
    String selectedPage = "Students";
    GlobalFunctions GF = new GlobalFunctions();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_student.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
        GF.sideBarClick(main_container, "/FrontEnds/StudentPage.fxml");
    }

    @FXML
    private void switchStudent(ActionEvent event) {
        if (!"Students".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/StudentPage.fxml");
            btn_student.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_venue.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_course.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_lecturer.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            selectedPage = "Students";
        }
    }

    @FXML
    private void switchVenue(ActionEvent event) {
        if (!"Venue".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/VenuePage.fxml");
            btn_venue.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_student.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_course.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_lecturer.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            selectedPage = "Venue";
        }
    }

    @FXML
    private void switchCourses(ActionEvent event) {
        if (!"Course".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/CoursePage.fxml");
            btn_course.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_student.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_venue.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_lecturer.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            selectedPage = "Course";
        }
    }

    @FXML
    private void switchLecturer(ActionEvent event) {
        if (!"Lecturer".equals(selectedPage)) {
            GF.sideBarClick(main_container, "/FrontEnds/LecturersPage.fxml");
            btn_lecturer.setStyle("-fx-background-color: #AC637C;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 3px 0px;");
            btn_student.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_course.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            btn_venue.setStyle("-fx-background-color: transparent;-fx-border-color: #ffffff;-fx-border-width: 0px 0px 0px 0px;");
            selectedPage = "Lecturer";
        }
    }

}
