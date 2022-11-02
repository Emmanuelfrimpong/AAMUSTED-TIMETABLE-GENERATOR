package Controllers;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import MongoServices.DatabaseServices;
import MongoServices.TableGeneration;
import Objects.Configuration;
import Objects.CoursesObject;
import Objects.StudentsObject;
import Objects.TableObject;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class TablesPageController implements Initializable {

    @FXML
    private JFXButton btn_generate;

    /**
     * Initializes the controller class.
     */
    GlobalFunctions GF = new GlobalFunctions();
    Configuration config = new Configuration();
    DatabaseServices DBservices = new DatabaseServices();
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private ComboBox<String> cmb_filter;
    @FXML
    private JFXButton btn_export;
    FilteredList<TableObject> filteredList;
    ObservableList<TableObject> tables;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = DBservices.getConfig();
         tables = DBservices.getTables();
         filteredList = new FilteredList<>(tables);
        getTable(filteredList);
    }

    private void getTable(FilteredList<TableObject> list) {
        cmb_filter.getItems().clear();
        cmb_filter.getItems().addAll("Regular", "Evening", "Weekend", "Level 100", "Level 200", "Level 300", "Level 400");
        
        
        ObservableList<TableObject> mondayList = FXCollections.observableArrayList();
        ObservableList<TableObject> tuesdayList = FXCollections.observableArrayList();
        ObservableList<TableObject> wednesdayList = FXCollections.observableArrayList();
        ObservableList<TableObject> thursdayList = FXCollections.observableArrayList();
        ObservableList<TableObject> fridayList = FXCollections.observableArrayList();
        ObservableList<TableObject> saturdayList = FXCollections.observableArrayList();
        ObservableList<TableObject> sundayList = FXCollections.observableArrayList();
        List<ObservableList<TableObject>> overALl = new ArrayList<>();
        for (TableObject tb : list) {
            if (tb.getDay().equals("Monday")) {
                mondayList.add(tb);
            }
            if (tb.getDay().equals("Tuesday")) {
                tuesdayList.add(tb);
            }
            if (tb.getDay().equals("Wednesday")) {
                wednesdayList.add(tb);
            }
            if (tb.getDay().equals("Thursday")) {
                thursdayList.add(tb);
            }
            if (tb.getDay().equals("Friday")) {
                fridayList.add(tb);
            }
            if (tb.getDay().equals("Saturday")) {
                saturdayList.add(tb);
            }
            if (tb.getDay().equals("Sunday")) {
                sundayList.add(tb);
            }
        }

        if (!mondayList.isEmpty()) {
            overALl.add(mondayList);
        }
        if (!tuesdayList.isEmpty()) {
            overALl.add(tuesdayList);
        }
        if (!wednesdayList.isEmpty()) {
            overALl.add(wednesdayList);
        }
        if (!thursdayList.isEmpty()) {
            overALl.add(thursdayList);
        }
        if (!fridayList.isEmpty()) {
            overALl.add(fridayList);
        }
        if (!saturdayList.isEmpty()) {
            overALl.add(saturdayList);
        }
        if (!sundayList.isEmpty()) {
            overALl.add(sundayList);
        }

        try {
            for (int i = 0; i < overALl.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FrontEnds/TableItem.fxml"));
                StackPane anchorPane = fxmlLoader.load();
                TableItemController itemController = fxmlLoader.getController();
                itemController.setData(overALl.get(i));
                grid.add(anchorPane, 1, i + 1); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerate(ActionEvent evt) {
        TableGeneration TG = new TableGeneration();
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        LoadingDailog loading = new LoadingDailog("Generating Tables........");
        if (config == null || config.getDays() == null || config.getPeriods() == null) {
            GF.inforAlert("Invalid Information", "Stettings not properly set.Set configurattions in Settings", Alert.AlertType.ERROR);
        } else {
            List<String> departments = new ArrayList();
            departments.add("2022/2023 - 1st Semester");
            departments.add("2022/2023 - 2st Semester");
            departments.add("2023/2024 - 1st Semester");
            departments.add("2023/2024 - 2st Semester");
            departments.add("2024/2025 - 1st Semester");
            departments.add("2024/2025 - 2st Semester");
            departments.add("2025/2026 - 1st Semester");
            departments.add("2025/2026 - 2st Semester");
            departments.add("2026/2027 - 1st Semester");
            departments.add("2026/2027 - 2st Semester");
            departments.add("2027/2028 - 1st Semester");
            departments.add("2027/2028 - 2st Semester");
            departments.add("2028/2029 - 1st Semester");
            departments.add("2028/2029 - 2st Semester");
            departments.add("2029/2030 - 1st Semester");
            departments.add("2029/2030 - 2st Semester");
            departments.add("2030/2031 - 1st Semester");
            departments.add("2030/2031 - 2st Semester");
            ButtonType submit = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Dialog<String> dialog = new Dialog<>();
            dialog.setHeaderText("Select Academic semester and proceed");
            DialogPane dialogPane = dialog.getDialogPane();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialogPane.getStylesheets().add("/Styles/dialogStyle.css");
            dialogPane.getButtonTypes().addAll(submit, cancel);
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(departments);
            comboBox.getSelectionModel().selectFirst();
            dialogPane.setContent(new VBox(comboBox));
            dialog.setResultConverter((ButtonType button) -> {
                if (button == submit) {
                    return comboBox.getValue();
                }
                return null;
            });
            Optional<String> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((String results) -> {
                Service<  ObservableList<TableObject>> service = new Service<  ObservableList<TableObject>>() {
                    @Override
                    protected Task< ObservableList<TableObject>> createTask() {
                        return new Task<  ObservableList<TableObject>>() {
                            @Override
                            protected ObservableList<TableObject> call() throws Exception {
                                return TG.getData();
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
                    tables = service.getValue();
                     filteredList = new FilteredList<>(tables);
                    getTable(filteredList);
                    GF.showToast("Table Successfully Generated", stage);
                });
                service.start();

                // DBservices.GenerateTable(results);
            });
        }

    }

    @FXML
    private void handleOnFilter(ActionEvent event) {
        if (cmb_filter.getValue().isEmpty()) {
            filteredList.setPredicate(null);
        } else {
            System.out.println("Item to filter=============="+cmb_filter.getValue().toLowerCase());
            Predicate<TableObject> filter
                    = i ->i.getType()!=null&&!i.getType().isEmpty()&& i.getLevel().toLowerCase().contains(cmb_filter.getValue())
                            ||i.getType()!=null&&!i.getType().isEmpty()&& i.getType().toLowerCase().contains(cmb_filter.getValue().toLowerCase());
            filteredList.setPredicate(filter);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       GF.showToast("This Feature is not yet Implemented", stage);
    }

    @FXML
    private void handleExport(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GF.showToast("This Feature is not yet Implemented", stage);
    }

}
