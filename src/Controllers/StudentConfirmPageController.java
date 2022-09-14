package Controllers;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.RingProgressIndicator;
import MongoServices.DatabaseServices;
import Objects.InitialStudentData;
import Objects.StudentsObject;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class StudentConfirmPageController implements Initializable {

    @FXML
    private ComboBox<String> cmb_department;
    @FXML
    private JFXButton btn_save;
    @FXML
    private JFXButton btn_cancel;
    @FXML
    private TableView<InitialStudentData> previewTable;
    @FXML
    private TableColumn<InitialStudentData, String> col_level;
    @FXML
    private TableColumn<InitialStudentData, String> col_name;
    @FXML
    private TableColumn<InitialStudentData, String> col_size;
    @FXML
    private TableColumn<InitialStudentData, String> col_hasDisable;
    @FXML
    private TableColumn<InitialStudentData, List<String>> col_courses;

    DatabaseServices DS = new DatabaseServices();
    GlobalFunctions GF = new GlobalFunctions();
    Stage parentStage;
    ObservableList<StudentsObject> tableList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Stage stage = (Stage) btn_save.getScene().getWindow();
//       ObservableList<InitialStudentData> data = (ObservableList<InitialStudentData>) stage.getUserData();
        //showData(data);
        List<String> departments = new ArrayList();
        departments.add("Catering & Hospitality Education");
        departments.add("Fashion & Textiles Design Education");
        departments.add("Construction & Wood Technology Education");
        departments.add("Automotive & Mechnical Technology Education");
        departments.add("Electrical & Electronics Technology Education");
        departments.add("Languages Education");
        departments.add("Interdisciplinary Studies");
        departments.add("Accounting Studies");
        departments.add("Management Studies");
        departments.add("Information Technology Education");
        departments.add("Mathematics Education");
        departments.add("Others");
        cmb_department.getItems().addAll(departments);

    }

    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        RingProgressIndicator indicator = new RingProgressIndicator();
        indicator.makeIndeterminate();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        VBox cont = new VBox();
        cont.setAlignment(Pos.CENTER);
        Label lb = new Label("Saving Data..........");
        cont.getChildren().addAll(indicator, lb);
        Scene scene = new Scene(cont);
        Stage loadingStage = new Stage();
        loadingStage.setX(bounds.getMinX());
        loadingStage.setY(bounds.getMinY());
        loadingStage.setWidth(bounds.getWidth());
        loadingStage.setHeight(bounds.getHeight());
        loadingStage.setOpacity(0.95);
        loadingStage.setResizable(false);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        loadingStage.setScene(scene);
        loadingStage.show();
        StudentsObject studentData = new StudentsObject();
        ObservableList<InitialStudentData> initData = previewTable.getItems();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm a").format(Calendar.getInstance().getTime());
        for (InitialStudentData data : initData) {
            studentData.setCourses(data.getCourses());
            studentData.setCreatedAt(timeStamp);
            studentData.setDepartment(cmb_department.getValue());
            studentData.setHasDisability(data.getDisability());
            studentData.setLevel(data.getLevel());
            studentData.setName(data.getName().toUpperCase());
            studentData.setSize(data.getSize());
            tableList = DS.saveStudentClass(studentData);

        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontEnds/StudentPage.fxml"));
        Parent root = fxmlLoader.load();
        StudentPageController controller = fxmlLoader.<StudentPageController>getController();
        controller.getStudentsData();
        loadingStage.close();
        GF.showToast("Data saved Succefully", parentStage);
        stage.close();

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showData(ObservableList<InitialStudentData> data, Stage stage) {
        parentStage = stage;
        col_level.setCellValueFactory(new PropertyValueFactory<>("level"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_size.setCellValueFactory(new PropertyValueFactory<>("size"));
        col_hasDisable.setCellValueFactory(new PropertyValueFactory<>("disability"));
        col_courses.setCellValueFactory(new PropertyValueFactory<>("courses"));
        previewTable.setItems(data);

    }

    @FXML
    private void handleDepSelected(ActionEvent event) {
        if (cmb_department.getValue() != null) {
            btn_save.setVisible(true);

        } else {
            btn_save.setVisible(false);
        }
    }

    public void loadData(ObservableList<StudentsObject> list) {
        this.tableList = list;
    }

}
