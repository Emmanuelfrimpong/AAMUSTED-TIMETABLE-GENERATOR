package Controllers;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import Objects.Configuration;
import Objects.TableObject;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.layout.VBox;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = DBservices.getConfig();
        getTable();
    }

    private void getTable() {
        ObservableList<TableObject> tables = DBservices.getTables();     
         ObservableList<TableObject> monday=FXCollections.observableArrayList();
          ObservableList<TableObject> tuesday=FXCollections.observableArrayList();
           ObservableList<TableObject> wednesday=FXCollections.observableArrayList();
            ObservableList<TableObject> thursday=FXCollections.observableArrayList();
             ObservableList<TableObject> friday=FXCollections.observableArrayList();
             List<ObservableList<TableObject>>overALl=new ArrayList<>();
             for(TableObject tb:tables){
                 if(tb.getDay().equals("Monday")){
                   
                     monday.add(tb);
                    }
                    if(tb.getDay().equals("Tuesday")){
                        tuesday.add(tb);
                    }

                    if(tb.getDay().equals("Wednesday")){
                        wednesday.add(tb);
                    }

                    if(tb.getDay().equals("Thursday")){
                        thursday.add(tb);
                    }

                    if(tb.getDay().equals("Friday")){
                        friday.add(tb);
                    }        
             }
             int size=0;
             if(!monday.isEmpty()){
                 size++;
                 overALl.add(monday);
             }
                if(!tuesday.isEmpty()){
                    size++;
                    overALl.add(tuesday);
                }
                if(!wednesday.isEmpty()){
                    size++;
                    overALl.add(wednesday);
                }
                if(!thursday.isEmpty()){
                    size++;
                    overALl.add(thursday);
                }
                if(!friday.isEmpty()){
                    size++;
                    overALl.add(friday);
                }
                
             
        int column = 0;
        int row = 1;
        try {
          
            for (int i = 0; i < overALl.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FrontEnds/TableItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TableItemController itemController = fxmlLoader.getController();
                itemController.setData(overALl.get(i));               
                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerate(ActionEvent event) {
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
            dialogPane.setContent(new VBox(8, comboBox));
            dialog.setResultConverter((ButtonType button) -> {
                if (button == submit) {
                    return comboBox.getValue();
                }
                return null;
            });

            Optional<String> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((String results) -> {
                DBservices.GenerateTable(results);
            });
        }

    }

}
