
package Controllers;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import MongoServices.DatabaseServices;
import Objects.Configuration;
import Objects.CoursesObject;
import Objects.DaysObject;
import Objects.PeriodsObject;
import Objects.SpecialVenue;
import Objects.Venue;
import com.jfoenix.controls.JFXComboBox;
import com.mongodb.client.MongoCursor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private Button btn_clear;
    @FXML
    private CheckBox monREG;
    @FXML
    private CheckBox monEVE;
    @FXML
    private CheckBox monWND;
    @FXML
    private CheckBox tueREG;
    @FXML
    private CheckBox tueEVE;
    @FXML
    private CheckBox tueWND;
    @FXML
    private CheckBox wedEVE;
    @FXML
    private CheckBox wedWND;
    @FXML
    private CheckBox thuREG;
    @FXML
    private CheckBox thuEVE;
    @FXML
    private CheckBox thuWND;
    @FXML
    private CheckBox friREG;
    @FXML
    private CheckBox friEVE;
    @FXML
    private CheckBox friWND;
    @FXML
    private CheckBox satREG;
    @FXML
    private CheckBox satEVE;
    @FXML
    private CheckBox satWND;
    @FXML
    private CheckBox sunREG;
    @FXML
    private CheckBox sunEVE;
    @FXML
    private CheckBox sunWND;
    @FXML
    private CheckBox firstREG;
    @FXML
    private CheckBox firstEVE;
    @FXML
    private CheckBox firstWND;
    @FXML
    private CheckBox secondREG;
    @FXML
    private CheckBox secondEVE;
    @FXML
    private CheckBox thirdREG;
    @FXML
    private CheckBox thirdEVE;
    @FXML
    private CheckBox forthEVE;
    @FXML
    private CheckBox forthREG;
    @FXML
    private CheckBox forthWND;
    @FXML
    private CheckBox secondWND;
    @FXML
    private CheckBox thirdWND;
    @FXML
    private CheckBox wedREG;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> time = new ArrayList<>();
        int after = 1;
        for (int i = 5; i < 23; i++) {
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
                    cm_course.getItems().add(course.getTitle() + " - " + course.getCode());
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
            List<ObservableMap<String, Object>> days = config.getDays();
            List<ObservableMap<String, Object>> periods = config.getPeriods();
            for (int i=0;i<periods.size();i++) {    
                Document doc=(Document)periods.get(i);     
                PeriodsObject PO = PeriodsObject.fromDocument(doc);
                if (PO.getPeriod().equals("1st Period")) {
                    firstStart.getSelectionModel().select(PO.getStartTime());
                    firstEnd.getSelectionModel().select(PO.getEndTime());
                    firstP.setSelected(true);
                    firstStart.setVisible(true);
                    firstEnd.setVisible(true);
                    firstREG.setSelected(PO.isReg());
                    firstREG.setVisible(true);
                    firstEVE.setSelected(PO.isEve());
                    firstEVE.setVisible(true);
                    firstWND.setSelected(PO.isWnd());
                    firstWND.setVisible(true);
                }

                if (PO.getPeriod().equals("2nd Period")) {
                    secondStart.getSelectionModel().select(PO.getStartTime());
                    secondEnd.getSelectionModel().select(PO.getEndTime());
                    secondP.setSelected(true);
                    secondStart.setVisible(true);
                    secondEnd.setVisible(true);
                    secondREG.setSelected(PO.isReg());
                    secondREG.setVisible(true);
                    secondEVE.setSelected(PO.isEve());
                    secondEVE.setVisible(true);
                    secondWND.setSelected(PO.isWnd());
                    secondWND.setVisible(true);
                }

                if (PO.getPeriod().equals("3rd Period")) {
                    thirdStart.getSelectionModel().select(PO.getStartTime());
                    thirdEnd.getSelectionModel().select(PO.getEndTime());
                    thirdP.setSelected(true);
                    thirdStart.setVisible(true);
                    thirdEnd.setVisible(true);
                    thirdREG.setSelected(PO.isReg());
                    thirdREG.setVisible(true);
                    thirdEVE.setSelected(PO.isEve());
                    thirdEVE.setVisible(true);
                    thirdWND.setSelected(PO.isWnd());
                    thirdWND.setVisible(true);
                }

                if (PO.getPeriod().equals("4th Period")) {
                    fourthStart.getSelectionModel().select(PO.getStartTime());
                    fourthEnd.getSelectionModel().select(PO.getEndTime());
                    fourthP.setSelected(true);
                    fourthStart.setVisible(true);
                    fourthEnd.setVisible(true);
                    forthREG.setSelected(PO.isReg());
                    forthREG.setVisible(true);
                    forthEVE.setSelected(PO.isEve());
                    forthEVE.setVisible(true);
                    forthWND.setSelected(PO.isWnd());
                    forthWND.setVisible(true);
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
                for (int j=0;j<days.size();j++) {
                    Document doc=(Document)days.get(j);
                    DaysObject day = DaysObject.fromDocument(doc);
                    if (day.getDay().equals("Monday")) {
                        checkMonday.setSelected(true);
                        monREG.setVisible(true);
                        monEVE.setVisible(true);
                        monWND.setVisible(true);
                        monREG.setSelected(day.isReg());
                        monEVE.setSelected(day.isEve());
                        monWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Tuesday")){
                        checkTuesday.setSelected(true);
                        tueREG.setVisible(true);
                        tueEVE.setVisible(true);
                        tueWND.setVisible(true);
                        tueREG.setSelected(day.isReg());
                        tueEVE.setSelected(day.isEve());
                        tueWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Wednesday")){
                        checkWednesday.setSelected(true);
                        wedREG.setVisible(true);
                        wedEVE.setVisible(true);
                        wedWND.setVisible(true);
                        wedREG.setSelected(day.isReg());
                        wedEVE.setSelected(day.isEve());
                        wedWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Thursday")){
                        checkThursday.setSelected(true);
                        thuREG.setVisible(true);
                        thuEVE.setVisible(true);
                        thuWND.setVisible(true);
                        thuREG.setSelected(day.isReg());
                        thuEVE.setSelected(day.isEve());
                        thuWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Friday")){
                        checkFriday.setSelected(true);
                        friREG.setVisible(true);
                        friEVE.setVisible(true);
                        friWND.setVisible(true);
                        friREG.setSelected(day.isReg());
                        friEVE.setSelected(day.isEve());
                        friWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Saturday")){
                        checkSaturday.setSelected(true);
                        satREG.setVisible(true);
                        satEVE.setVisible(true);
                        satWND.setVisible(true);
                        satREG.setSelected(day.isReg());
                        satEVE.setSelected(day.isEve());
                        satWND.setSelected(day.isWnd());
                    }

                    if(day.getDay().equals("Sunday")){
                        checkSunday.setSelected(true);
                        sunREG.setVisible(true);
                        sunEVE.setVisible(true);
                        sunWND.setVisible(true);
                        sunREG.setSelected(day.isReg());
                        sunEVE.setSelected(day.isEve());
                        sunWND.setSelected(day.isWnd());
                    }
                }        
            }

        }
    }

    @FXML
    private void onSaveConfig(ActionEvent event) {
        LoadingDailog loading = new LoadingDailog("Saving Configurations........");
        Stage stage = (Stage) btn_save.getScene().getWindow();
        if (getSelectedPeriod() != null && !getSelectedPeriod().isEmpty()) {
            config.setPeriods(getSelectedPeriod());
            if (handleDaySelect() != null && !handleDaySelect().isEmpty()) {
                config.setDays(handleDaySelect());
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
                        Service<List<MongoCursor<Document>>> service = new Service<List<MongoCursor<Document>>>() {
                            @Override
                            protected Task<List<MongoCursor<Document>>> createTask() {
                                return new Task<List<MongoCursor<Document>>>() {
                                    @Override
                                    protected List<MongoCursor<Document>> call() throws Exception {
                                        return DBservices.saveConfig(config); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                    }
                                };

                            }
                        };

                        service.setOnFailed((WorkerStateEvent evt) -> {
                            System.out.println("Failed==========================================");
                            loading.close();

                        });
                        service.setOnRunning((WorkerStateEvent evt) -> loading.show());
                        service.setOnSucceeded((WorkerStateEvent evt) -> {
                            loading.close();
                            GF.showToast("Configuration Successfully Saved", stage);
                            loadConfig();
                        });
                        service.start();

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

 
    @FXML
    private void HandleMondaySelect(ActionEvent event) {
        if (checkMonday.isSelected()) {
            monREG.setVisible(true);
            monEVE.setVisible(true);
            monWND.setVisible(true);

        } else {

            monREG.setVisible(false);
            monEVE.setVisible(false);
            monWND.setVisible(false);

        }
    }

    @FXML
    private void HandleTuesdaySelect(ActionEvent event) {
        if (checkTuesday.isSelected()) {

            tueREG.setVisible(true);
            tueEVE.setVisible(true);
            tueWND.setVisible(true);

        } else {

            tueREG.setVisible(false);
            tueEVE.setVisible(false);
            tueWND.setVisible(false);

        }
    }

    @FXML
    private void HandleWednesdaySelect(ActionEvent event) {
        if (checkWednesday.isSelected()) {

            wedREG.setVisible(true);
            wedEVE.setVisible(true);
            wedWND.setVisible(true);

        } else {

            wedREG.setVisible(false);
            wedEVE.setVisible(false);
            wedWND.setVisible(false);

        }
    }

    @FXML
    private void HandleThursdaySelect(ActionEvent event) {
        if (checkThursday.isSelected()) {

            thuREG.setVisible(true);
            thuEVE.setVisible(true);
            thuWND.setVisible(true);

        } else {

            thuREG.setVisible(false);
            thuEVE.setVisible(false);
            thuWND.setVisible(false);

        }
    }

    @FXML
    private void HandleFridaySelect(ActionEvent event) {
        if (checkFriday.isSelected()) {

            friREG.setVisible(true);
            friEVE.setVisible(true);
            friWND.setVisible(true);

        } else {

            friREG.setVisible(false);
            friEVE.setVisible(false);
            friWND.setVisible(false);

        }
    }

    @FXML
    private void HandleSaturdaySelect(ActionEvent event) {
        if (checkSaturday.isSelected()) {

            satREG.setVisible(true);
            satEVE.setVisible(true);
            satWND.setVisible(true);

        } else {

            satREG.setVisible(false);
            satEVE.setVisible(false);
            satWND.setVisible(false);

        }
    }

    @FXML
    private void HandleSundaySelect(ActionEvent event) {
        if (checkSunday.isSelected()) {

            sunREG.setVisible(true);
            sunEVE.setVisible(true);
            sunWND.setVisible(true);

        } else {

            sunREG.setVisible(false);
            sunEVE.setVisible(false);
            sunWND.setVisible(false);

        }
    }

    @FXML
    private void HandleFirstPeriod(ActionEvent event) {
        if (firstP.isSelected()) {
            firstStart.setVisible(true);
            firstEnd.setVisible(true);
            firstREG.setVisible(true);
            firstEVE.setVisible(true);
            firstWND.setVisible(true);

        } else {
            firstStart.setVisible(false);
            firstEnd.setVisible(false);
            firstREG.setVisible(false);
            firstEVE.setVisible(false);
            firstWND.setVisible(false);

        }

    }

    @FXML
    private void HandleSecondPeriod(ActionEvent event) {
        if (secondP.isSelected()) {
            secondStart.setVisible(true);
            secondEnd.setVisible(true);
            secondREG.setVisible(true);
            secondEVE.setVisible(true);
            secondWND.setVisible(true);

        } else {
            secondStart.setVisible(false);
            secondEnd.setVisible(false);
            secondREG.setVisible(false);
            secondEVE.setVisible(false);
            secondWND.setVisible(false);

        }
    }

    @FXML
    private void HandleThirdPeriod(ActionEvent event) {
        if (thirdP.isSelected()) {
            thirdStart.setVisible(true);
            thirdEnd.setVisible(true);
            thirdREG.setVisible(true);
            thirdEVE.setVisible(true);
            thirdWND.setVisible(true);
        } else {
            thirdStart.setVisible(false);
            thirdEnd.setVisible(false);
            thirdREG.setVisible(false);
            thirdEVE.setVisible(false);
            thirdWND.setVisible(false);

        }
    }

    @FXML
    private void HandleFourthPeriod(ActionEvent event) {
        if (fourthP.isSelected()) {
            fourthStart.setVisible(true);
            fourthEnd.setVisible(true);
            forthREG.setVisible(true);
            forthEVE.setVisible(true);
            forthWND.setVisible(true);
        } else {
            fourthStart.setVisible(false);
            fourthEnd.setVisible(false);
            forthREG.setVisible(false);
            forthEVE.setVisible(false);
            forthWND.setVisible(false);

        }
    }

    void ClearConfig() {
        checkMonday.setSelected(false);
        checkTuesday.setSelected(false);
        checkWednesday.setSelected(false);
        checkThursday.setSelected(false);
        checkFriday.setSelected(false);
        checkSaturday.setSelected(false);
        checkSunday.setSelected(false);
        firstP.setSelected(false);
        secondP.setSelected(false);
        thirdP.setSelected(false);
        fourthP.setSelected(false);
        firstStart.setVisible(false);
        firstEnd.setVisible(false);
        secondStart.setVisible(false);
        secondEnd.setVisible(false);
        thirdStart.setVisible(false);
        thirdEnd.setVisible(false);
        fourthStart.setVisible(false);
        fourthEnd.setVisible(false);
        firstStart.setValue(null);
        firstEnd.setValue(null);
        secondStart.setValue(null);
        secondEnd.setValue(null);
        thirdStart.setValue(null);
        thirdEnd.setValue(null);
        fourthStart.setValue(null);
        fourthEnd.setValue(null);
        firstREG.setSelected(false);
        firstEVE.setSelected(false);
        firstWND.setSelected(false);
        secondREG.setSelected(false);
        secondEVE.setSelected(false);
        secondWND.setSelected(false);
        thirdREG.setSelected(false);
        thirdEVE.setSelected(false);
        thirdWND.setSelected(false);
        forthREG.setSelected(false);
        forthEVE.setSelected(false);
        forthWND.setSelected(false);
        firstREG.setVisible(false);
        firstEVE.setVisible(false);
        firstWND.setVisible(false);
        secondREG.setVisible(false);
        secondEVE.setVisible(false);
        secondWND.setVisible(false);
        thirdREG.setVisible(false);
        thirdEVE.setVisible(false);
        thirdWND.setVisible(false);
        forthREG.setVisible(false);
        forthEVE.setVisible(false);
        forthWND.setVisible(false);

    }

    @FXML
    private void HandleCourseSelection(ActionEvent event) {
        if (cm_course.getValue() != null && cm_venue.getValue() != null) {
            String val = cm_course.getValue().replaceAll("\\s+", "").split("-")[1].replaceAll("\\s+", "");
            SpecialVenue van = new SpecialVenue(val, cm_venue.getValue());

            boolean exist = false;
            for (SpecialVenue venue : tv_special.getItems()) {
                if (venue.getCourse().equals(val)) {
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
                if (firstREG.isSelected() || firstEVE.isSelected() || firstWND.isSelected()) {
                    PeriodsObject p = new PeriodsObject();
                    p.setPeriod("1st Period");
                    p.setEndTime(firstEnd.getValue());
                    p.setStartTime(firstStart.getValue());
                    p.setEve(firstEVE.isSelected());
                    p.setReg(firstREG.isSelected());
                    p.setWnd(firstWND.isSelected());                
                    ListOfPeriods.add(p.toMap());
                } else {
                    GF.inforAlert("Incomplet data", "Please Select at least one category of student for Fist Period", Alert.AlertType.WARNING);
                }
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 1st Period", Alert.AlertType.WARNING);
            }
        }

        if (secondP.isSelected()) {
            if (secondStart.getValue() != null && secondEnd.getValue() != null) {
                if (secondREG.isSelected() || secondEVE.isSelected() || secondWND.isSelected()) {
                    PeriodsObject p = new PeriodsObject();
                    p.setPeriod("2nd Period");
                    p.setEndTime(secondEnd.getValue());
                    p.setStartTime(secondStart.getValue());
                    p.setEve(secondEVE.isSelected());
                    p.setReg(secondREG.isSelected());
                    p.setWnd(secondWND.isSelected());
                    ListOfPeriods.add(p.toMap());
                } else {
                    GF.inforAlert("Incomplet data", "Please Select at least one category of student for Second Period", Alert.AlertType.WARNING);
                }
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 2nd Period", Alert.AlertType.WARNING);
            }
        }

        if (thirdP.isSelected()) {
            if (thirdStart.getValue() != null && thirdEnd.getValue() != null) {
                if (thirdREG.isSelected() || thirdEVE.isSelected() || thirdWND.isSelected()) {
                    PeriodsObject p = new PeriodsObject();
                    p.setPeriod("3rd Period");
                    p.setEndTime(thirdEnd.getValue());
                    p.setStartTime(thirdStart.getValue());
                    p.setEve(thirdEVE.isSelected());
                    p.setReg(thirdREG.isSelected());
                    p.setWnd(thirdWND.isSelected());
                     ListOfPeriods.add(p.toMap());
                } else {
                    GF.inforAlert("Incomplet data", "Please Select at least one category of student for Third Period", Alert.AlertType.WARNING);
                }
            } else {
                GF.inforAlert("Incomplet data", "Please Set start and End time for 3rd Period", Alert.AlertType.WARNING);
            }
        }

        if (fourthP.isSelected()) {
            if (fourthStart.getValue() != null && fourthEnd.getValue() != null) {
                if (forthREG.isSelected() || forthEVE.isSelected() || forthWND.isSelected()) {
                    PeriodsObject p = new PeriodsObject();
                    p.setPeriod("4th Period");
                    p.setEndTime(fourthEnd.getValue());
                    p.setStartTime(fourthStart.getValue());

                    p.setEve(forthEVE.isSelected());
                    p.setReg(forthREG.isSelected());
                    p.setWnd(forthWND.isSelected());
                    ListOfPeriods.add(p.toMap());
                } else {
                    GF.inforAlert("Incomplet data", "Please Select at least one category of student for Fourth Period", Alert.AlertType.WARNING);
                }
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
                        listOfSpecialVenue.add(new SpecialVenue(venue.getCourse(), venue.getVenue()).getSpecialVenueMap());
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

    @FXML
    private void onClear(ActionEvent event) {
        LoadingDailog loading = new LoadingDailog("Deleting Configurations........");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to Delete all configurations. Note that all tables under this config will be deleted.",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Delete Data");
        alert.getDialogPane().getStylesheets().add("/Styles/dialogStyle.css");
        alert.getDialogPane().setMinSize(400, 200);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Service<Document> service = new Service<Document>() {
                @Override
                protected Task<Document> createTask() {
                    return new Task<Document>() {
                        @Override
                        protected Document call() throws Exception {
                            return DBservices.DeleteConfig(config); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        }
                    };

                }
            };

            service.setOnFailed((WorkerStateEvent evt) -> {
                loading.close();
            });
            service.setOnRunning((WorkerStateEvent evt) -> loading.show());
            service.setOnSucceeded((WorkerStateEvent evt) -> {
                loading.close();
                GF.inforAlert("Data Delete", "Configuration Successfully Delete", Alert.AlertType.INFORMATION);
                ClearConfig();
                loadConfig();
            });
            service.start();
        }
    }

    private List<ObservableMap<String, Object>> handleDaySelect() {
        List<ObservableMap<String, Object>> list = new ArrayList();
        if (checkMonday.isSelected()) {
            DaysObject day = new DaysObject();
            if (monREG.isSelected() || monEVE.isSelected() || monWND.isSelected()) {
                day.setDay("Monday");
                day.setReg(monREG.isSelected());
                day.setEve(monEVE.isSelected());
                day.setWnd(monWND.isSelected());
                 list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Monday", Alert.AlertType.WARNING);
            }
        }

        if (checkTuesday.isSelected()) {
            DaysObject day = new DaysObject();
            if (tueREG.isSelected() || tueEVE.isSelected() || tueWND.isSelected()) {
                day.setDay("Tuesday");
                day.setReg(tueREG.isSelected());
                day.setEve(tueEVE.isSelected());
                day.setWnd(tueWND.isSelected());
                 list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Tuesday", Alert.AlertType.WARNING);
            }
        }

        if (checkWednesday.isSelected()) {
            DaysObject day = new DaysObject();
            if (wedREG.isSelected() || wedEVE.isSelected() || wedWND.isSelected()) {
                day.setDay("Wednesday");
                day.setReg(wedREG.isSelected());
                day.setEve(wedEVE.isSelected());
                day.setWnd(wedWND.isSelected());
                list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Wednesday", Alert.AlertType.WARNING);
            }
        }

        if (checkThursday.isSelected()) {
            DaysObject day = new DaysObject();
            if (thuREG.isSelected() || thuEVE.isSelected() || thuWND.isSelected()) {
                day.setDay("Thursday");
                day.setReg(thuREG.isSelected());
                day.setEve(thuEVE.isSelected());
                day.setWnd(thuWND.isSelected());
                 list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Thursday", Alert.AlertType.WARNING);
            }
        }

        if (checkFriday.isSelected()) {
            DaysObject day = new DaysObject();
            if (friREG.isSelected() || friEVE.isSelected() || friWND.isSelected()) {
                day.setDay("Friday");
                day.setReg(friREG.isSelected());
                day.setEve(friEVE.isSelected());
                day.setWnd(friWND.isSelected());
                 list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Friday", Alert.AlertType.WARNING);
            }
        }

        if (checkSaturday.isSelected()) {
            DaysObject day = new DaysObject();
            if (satREG.isSelected() || satEVE.isSelected() || satWND.isSelected()) {
                day.setDay("Saturday");
                day.setReg(satREG.isSelected());
                day.setEve(satEVE.isSelected());
                day.setWnd(satWND.isSelected());                           
                list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Saturday", Alert.AlertType.WARNING);
            }
        }

        if (checkSunday.isSelected()) {
            DaysObject day = new DaysObject();
            if (sunREG.isSelected() || sunEVE.isSelected() || sunWND.isSelected()) {
                day.setDay("Sunday");
                day.setReg(sunREG.isSelected());
                day.setEve(sunEVE.isSelected());
                day.setWnd(sunWND.isSelected());              
                 list.add(day.toMap());
            } else {
                GF.inforAlert("Incomplete Data", "Please Select category of Students for Sunday", Alert.AlertType.WARNING);
            }
        }
        return list;
    }
}
