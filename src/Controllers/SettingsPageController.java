/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import Objects.Configuration;
import Objects.CoursesObject;
import Objects.PeriodsObject;
import Objects.SpecialVenue;
import Objects.StudentsObject;
import Objects.Venue;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.Document;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class SettingsPageController implements Initializable {

    @FXML
    private Button btn_save;

    /**
     * Initializes the controller class.
     */
    private ObservableList<CoursesObject> coursesData;
    private ObservableList<CoursesObject> AllCourses;
    private ObservableList<Venue> venueData;
    ObservableMap<String, Object> data;
    GlobalFunctions GF = new GlobalFunctions();

    Configuration config = new Configuration();
    DatabaseServices DBservices = new DatabaseServices();
    String location = "";
    double[] xOffset = {0}, yOffset = {0};
    final ContextMenu contextMenu = new ContextMenu();
    MenuItem delete = new MenuItem("Remove");

    private Button btn_clse;
    @FXML
    private ComboBox<String> firstStart;
    @FXML
    private ComboBox<String> firstEnd;
    @FXML
    private ComboBox<String> secondStart;
    @FXML
    private ComboBox<String> secondEnd;
    @FXML
    private ComboBox<String> thirdStart;
    @FXML
    private ComboBox<String> thirdEnd;
    @FXML
    private ComboBox<String> fourthStart;
    @FXML
    private ComboBox<String> fourthEnd;
    @FXML
    private CheckBox checkMonday;
    @FXML
    private CheckBox checkTuesday;
    @FXML
    private CheckBox checkWednesday;
    @FXML
    private CheckBox checkThursday;
    @FXML
    private CheckBox checkFriday;
    @FXML
    private CheckBox checkSaturday;
    @FXML
    private CheckBox checkSunday;
    @FXML
    private CheckBox firstP;
    @FXML
    private CheckBox secondP;
    @FXML
    private CheckBox thirdP;
    @FXML
    private CheckBox fourthP;

    @FXML
    private TableView<SpecialVenue> tv_special;
    @FXML
    private TableColumn<SpecialVenue, String> col_course;
    @FXML
    private TableColumn<SpecialVenue, String> col_venue;
    @FXML
    private JFXComboBox<String> cm_course;
    @FXML
    private JFXComboBox<String> cm_venue;
    @FXML
    private JFXComboBox<String> cm_LAday;
    @FXML
    private JFXComboBox<String> cm_LAperiod;
    @FXML
    private Label lab_period;

    boolean liberalCourseExist = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> time = new ArrayList<>();
        int after = 1;
        for (int i = 5; i < 21; i++) {
            if (i < 12) {
                time.add(i + " am");
            } else if (i == 12) {
                time.add("12 pm");
            } else {
                time.add(after + " pm");
                after++;
            }

        }
        firstStart.getItems().addAll(time);
        firstEnd.getItems().addAll(time);
        secondStart.getItems().addAll(time);
        secondEnd.getItems().addAll(time);
        thirdStart.getItems().addAll(time);
        thirdEnd.getItems().addAll(time);
        fourthStart.getItems().addAll(time);
        fourthEnd.getItems().addAll(time);
        contextMenu.getItems().add(delete);
        tv_special.setContextMenu(contextMenu);
        delete.setOnAction((ActionEvent event) -> {
            if (!tv_special.getSelectionModel().isEmpty()) {
                tv_special.getItems().remove(tv_special.getSelectionModel().getSelectedIndex());
            }
        });
        cm_LAday.getItems().addAll("No Libral/African Studies", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Satuday", "Sunday");
        cm_LAperiod.setVisible(false);
        lab_period.setVisible(false);
        cm_LAperiod.getItems().addAll("1st Period", "2nd Period", "3rd Period", "4th Period");
        data = FXCollections.observableHashMap();
        coursesData = FXCollections.observableArrayList();
        venueData = FXCollections.observableArrayList();
        AllCourses = FXCollections.observableArrayList();
        data = DBservices.getDepartmentData("AAMUSTED_DB");
        venueData = DBservices.getVenues();
        if (data != null && !data.isEmpty()) {
            for (CoursesObject course : (ObservableList<CoursesObject>) data.get("courses")) {
                if (course.getDepartment().equals("Liberal/African Studies")) {
                    liberalCourseExist = true;
                }
                AllCourses.add(course);
                if (!"no".equals(course.getSpecialVenue().trim().toLowerCase())) {
                    coursesData.add(course);
                    cm_course.getItems().add(course.getTitle() + " - (" + course.getCode() + ")");
                }
            }
        }
        if (venueData != null && !venueData.isEmpty()) {
            for (Venue venue : venueData) {
                cm_venue.getItems().add(venue.getName());
            }
        }
        col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        col_venue.setCellValueFactory(new PropertyValueFactory<>("venue"));

        loadConfig();
    }

    private void loadConfig() {
        config = DBservices.getConfig();
        if (config != null) {
            cm_LAday.setValue(config.getLaDay());
            if (config.getLaPeriod() != null && !config.getLaPeriod().isEmpty() && !config.getLaDay().equals("No Libral/African Studies")) {
                cm_LAperiod.setValue(config.getLaPeriod());
                cm_LAperiod.setVisible(true);
                lab_period.setVisible(true);
            }
            List<String> days = config.getDays();
            List<ObservableMap<String, Object>> periods = config.getPeriods();
            for (Iterator<ObservableMap<String, Object>> it = periods.iterator(); it.hasNext();) {
                Document ob = (Document) it.next();
                PeriodsObject PO = PeriodsObject.getSpecialVenueFromDoc(ob);
                if (PO.getPeriod().equals("1st Period")) {
                    firstStart.getSelectionModel().select(PO.getStartTime());
                    firstEnd.getSelectionModel().select(PO.getEndTime());
                    firstP.setSelected(true);
                    firstStart.setVisible(true);
                    firstEnd.setVisible(true);
                }

                if (PO.getPeriod().equals("2nd Period")) {
                    secondStart.getSelectionModel().select(PO.getStartTime());
                    secondEnd.getSelectionModel().select(PO.getEndTime());
                    secondP.setSelected(true);
                    secondStart.setVisible(true);
                    secondEnd.setVisible(true);
                }

                if (PO.getPeriod().equals("3rd Period")) {
                    thirdStart.getSelectionModel().select(PO.getStartTime());
                    thirdEnd.getSelectionModel().select(PO.getEndTime());
                    thirdP.setSelected(true);
                    thirdStart.setVisible(true);
                    thirdEnd.setVisible(true);
                }

                if (PO.getPeriod().equals("4th Period")) {
                    fourthStart.getSelectionModel().select(PO.getStartTime());
                    fourthEnd.getSelectionModel().select(PO.getEndTime());
                    fourthP.setSelected(true);
                    fourthStart.setVisible(true);
                    fourthEnd.setVisible(true);
                }
            }
            List<ObservableMap<String, Object>> specialVenue = config.getSpecialVenue();
            if (specialVenue != null) {
                tv_special.getItems().clear();
                for (Iterator<ObservableMap<String, Object>> it = specialVenue.iterator(); it.hasNext();) {
                    Document ob = (Document) it.next();
                    SpecialVenue v = SpecialVenue.getSpecialVenueFromDoc(ob);
                    tv_special.getItems().add(v);
                }

            }
            if (days != null) {
                if (days.contains("Monday")) {
                    checkMonday.setSelected(true);
                }
                if (days.contains("Tuesday")) {
                    checkTuesday.setSelected(true);
                }
                if (days.contains("Wednesday")) {
                    checkWednesday.setSelected(true);
                }
                if (days.contains("Thursday")) {
                    checkThursday.setSelected(true);
                }
                if (days.contains("Friday")) {
                    checkFriday.setSelected(true);
                }
                if (days.contains("Saturday")) {
                    checkSaturday.setSelected(true);
                }
                if (days.contains("Sunday")) {
                    checkSunday.setSelected(true);
                }
            }

        }
    }

    @FXML
    private void onSaveConfig(ActionEvent event) {
        Stage stage = (Stage) btn_save.getScene().getWindow();
        if (getSelectedPeriod() != null && !getSelectedPeriod().isEmpty()) {
            config.setPeriods(getSelectedPeriod());
            if (config.getDays() != null && !config.getDays().isEmpty()) {
                if (checkSpecailVenue() != null) {
                    config.setSpecialVenue(checkSpecailVenue());

                    if (!liberalCourseExist && cm_LAday.getValue() != null || !cm_LAday.getValue().equals("No Libral/African Studies")) {
                        if (cm_LAday.getValue() != null && !cm_LAday.getValue().equals("No Libral/African Studies")) {
                            config.setLaDay(cm_LAday.getValue());
                            if (config.getLaPeriod() == null) {
                                GF.inforAlert("Incomplete data", "Select Period for Libral/African Studies", Alert.AlertType.WARNING);
                            }
                        } else {
                            config.setLaDay("No Libral/African Studies");
                            config.setLaPeriod("");
                        }
                        DBservices.saveConfig(config);
                        GF.inforAlert("Data Saved", "Configuration Successfully Saved", Alert.AlertType.INFORMATION);
                        loadConfig();
                        //Save the file
                    } else {
                        GF.inforAlert("Incomplete Data", "Please set Day and Period for Liberal Course", Alert.AlertType.ERROR);
                    }

                }
            } else {
                GF.inforAlert("Incomplete Data", "At Least a Day Should be Selected", Alert.AlertType.ERROR);
            }

        } else {
            GF.inforAlert("Incomplete Data", "At Least a Period Should be Added", Alert.AlertType.ERROR);
        }
    }

    public void setLocation(String new_Stage) {
        this.location = new_Stage;
        if (this.location != null && !this.location.isEmpty()) {
            btn_clse.setVisible(true);
        }
    }

    private void handleMouseDrag(MouseEvent event) {
        if (!this.location.isEmpty()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            }
        }
    }

    private void handleMousePress(MouseEvent event) {
        if (!this.location.isEmpty()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            }
        }
    }

    private void handleClose(ActionEvent event) {
        GF.closeWindow(event);
    }

    @FXML
    private void HandleMondaySelect(ActionEvent event) {
        if (checkMonday.isSelected()) {
            if (config.getDays() != null && !config.getDays().contains("Monday")) {
                config.getDays().add("Monday");
            }
        } else {
            if (config.getDays().contains("Monday")) {
                config.getDays().remove("Monday");
            }
        }
    }

    @FXML
    private void HandleTuesdaySelect(ActionEvent event) {
        if (checkTuesday.isSelected()) {
            if (!config.getDays().contains("Tuesday")) {
                config.getDays().add("Tuesday");
            }
        } else {
            if (config.getDays().contains("Tuesday")) {
                config.getDays().remove("Tuesday");
            }
        }
    }

    @FXML
    private void HandleWednesdaySelect(ActionEvent event) {
        if (checkWednesday.isSelected()) {
            if (!config.getDays().contains("Wednesday")) {
                config.getDays().add("Wednesday");
            }
        } else {
            if (config.getDays().contains("Wednesday")) {
                config.getDays().remove("Wednesday");
            }
        }
    }

    @FXML
    private void HandleThursdaySelect(ActionEvent event) {
        if (checkThursday.isSelected()) {
            if (!config.getDays().contains("Thursday")) {
                config.getDays().add("Thursday");
            }
        } else {
            if (config.getDays().contains("Thursday")) {
                config.getDays().remove("Thursday");
            }
        }
    }

    @FXML
    private void HandleFridaySelect(ActionEvent event) {
        if (checkFriday.isSelected()) {
            if (!config.getDays().contains("Friday")) {
                config.getDays().add("Friday");
            }
        } else {
            if (config.getDays().contains("Friday")) {
                config.getDays().remove("Friday");
            }
        }
    }

    @FXML
    private void HandleSaturdaySelect(ActionEvent event) {
        if (checkSaturday.isSelected()) {
            if (!config.getDays().contains("Saturday")) {
                config.getDays().add("Saturday");
            }
        } else {
            if (config.getDays().contains("Saturday")) {
                config.getDays().remove("Saturday");
            }
        }
    }

    @FXML
    private void HandleSundaySelect(ActionEvent event) {
        if (checkSunday.isSelected()) {
            if (!config.getDays().contains("Sunday")) {
                config.getDays().add("Sunday");
            }
        } else {
            if (config.getDays().contains("Sunday")) {
                config.getDays().remove("Sunday");
            }
        }
    }

    @FXML
    private void HandleFirstPeriod(ActionEvent event) {
        if (firstP.isSelected()) {
            firstStart.setVisible(true);
            firstEnd.setVisible(true);

        } else {
            firstStart.setVisible(false);
            firstEnd.setVisible(false);

        }

    }

    @FXML
    private void HandleSecondPeriod(ActionEvent event) {
        if (secondP.isSelected()) {
            secondStart.setVisible(true);
            secondEnd.setVisible(true);

        } else {
            secondStart.setVisible(false);
            secondEnd.setVisible(false);

        }
    }

    @FXML
    private void HandleThirdPeriod(ActionEvent event) {
        if (thirdP.isSelected()) {
            thirdStart.setVisible(true);
            thirdEnd.setVisible(true);
        } else {
            thirdStart.setVisible(false);
            thirdEnd.setVisible(false);

        }
    }

    @FXML
    private void HandleFourthPeriod(ActionEvent event) {
        if (fourthP.isSelected()) {
            fourthStart.setVisible(true);
            fourthEnd.setVisible(true);
        } else {
            fourthStart.setVisible(false);
            fourthEnd.setVisible(false);

        }
    }

    @FXML
    private void HandleCourseSelection(ActionEvent event) {
        if (cm_course.getValue() != null && cm_venue.getValue() != null) {
            SpecialVenue van = new SpecialVenue(cm_course.getValue(), cm_venue.getValue());
            boolean exist = false;
            for (SpecialVenue venue : tv_special.getItems()) {
                if (venue.getCourse().equals(cm_course.getValue())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                tv_special.getItems().add(van);
                cm_venue.setValue(null);
                cm_course.setValue(null);
            } else {
                GF.inforAlert("Duplicate ", "Course already asigned a special Venue", Alert.AlertType.INFORMATION);
                cm_venue.setValue(null);
                cm_course.setValue(null);
            }

        }
    }

    @FXML
    private void HandleLADaySelect(ActionEvent event) {
        if (cm_LAday.getValue() != null && !"No Libral/African Studies".equals(cm_LAday.getValue())) {
            cm_LAperiod.setVisible(true);
            lab_period.setVisible(true);
            config.setLaDay(cm_LAday.getValue());
        } else {
            lab_period.setVisible(false);
            cm_LAperiod.setVisible(false);
            config.setLaDay("No Libral/African Studies");
            config.setLaPeriod("");
        }
    }

    @FXML
    private void HandleLAPeriodSelect(ActionEvent event) {
        if (cm_LAperiod.getValue() != null) {
            config.setLaPeriod(cm_LAperiod.getValue());
        }
    }

    private List<ObservableMap<String, Object>> getSelectedPeriod() {
        List<ObservableMap<String, Object>> ListOfPeriods = new ArrayList<>();
        if (firstP.isSelected()) {
            if (firstStart.getValue() != null && firstEnd.getValue() != null) {
                PeriodsObject p = new PeriodsObject();
                p.setPeriod("1st Period");
                p.setEndTime(firstEnd.getValue());
                p.setStartTime(firstStart.getValue());
                ListOfPeriods.add(p.getPeriodsObjectMap());

            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 1st Period", Alert.AlertType.WARNING);
            }
        }

        if (secondP.isSelected()) {
            if (secondStart.getValue() != null && secondEnd.getValue() != null) {
                PeriodsObject p = new PeriodsObject();
                p.setPeriod("2nd Period");
                p.setEndTime(secondEnd.getValue());
                p.setStartTime(secondStart.getValue());
                ListOfPeriods.add(p.getPeriodsObjectMap());
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 2nd Period", Alert.AlertType.WARNING);
            }
        }

        if (thirdP.isSelected()) {
            if (thirdStart.getValue() != null && thirdEnd.getValue() != null) {
                PeriodsObject p = new PeriodsObject();
                p.setPeriod("3rd Period");
                p.setEndTime(thirdEnd.getValue());
                p.setStartTime(thirdStart.getValue());
                ListOfPeriods.add(p.getPeriodsObjectMap());
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 3rd Period", Alert.AlertType.WARNING);
            }
        }

        if (fourthP.isSelected()) {
            if (fourthStart.getValue() != null && fourthEnd.getValue() != null) {
                PeriodsObject p = new PeriodsObject();
                p.setPeriod("4th Period");
                p.setEndTime(fourthEnd.getValue());
                p.setStartTime(fourthStart.getValue());
                ListOfPeriods.add(p.getPeriodsObjectMap());
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 4th Period", Alert.AlertType.WARNING);
            }
        }
        return ListOfPeriods;

    }

    private List<ObservableMap<String, Object>> checkSpecailVenue() {
        List<ObservableMap<String, Object>> listOfSpecialVenue = new ArrayList<>();
        for (CoursesObject course : coursesData) {
            String text = course.getCode();
            if (tv_special.getItems() != null && !tv_special.getItems().isEmpty()) {
                for (SpecialVenue venue : tv_special.getItems()) {
                    if (text.equals(venue.getCourse())) {
                        listOfSpecialVenue.add(new SpecialVenue(course.getCode(), venue.getVenue()).getSpecialVenueMap());
                        break;
                    }
                }

            }
        }

        if (listOfSpecialVenue.size() != coursesData.size()) {
            GF.inforAlert("Incomplete data", "Some Couses Required Special Venue", Alert.AlertType.WARNING);
            return null;
        } else {
            return listOfSpecialVenue;
        }
    }

}
