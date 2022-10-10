package Controllers;

import Objects.TableIViewtem;
import Objects.TableObject;
import Objects.TableRowItem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class TableItemController implements Initializable {

    @FXML
    private Label lb_day;
    @FXML
    private GridPane grid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(ObservableList<TableObject> tables) {
        if (tables != null) {
            lb_day.setText(tables.get(0).getDay());
            List<ObservableList<TableObject>> AllSeperatePeriods = new ArrayList<>();
            ObservableList<TableObject> firstList = FXCollections.observableArrayList();
            ObservableList<TableObject> secondList = FXCollections.observableArrayList();
            ObservableList<TableObject> thirdList = FXCollections.observableArrayList();
            ObservableList<TableObject> fourthList = FXCollections.observableArrayList();
            for (TableObject to : tables) {
                switch (to.getPeriod()) {
                    case "1st Period":
                        firstList.add(to);
                        break;
                    case "2nd Period":
                        secondList.add(to);
                        break;
                    case "3rd Period":
                        thirdList.add(to);
                        break;
                    case "4th Period":
                        fourthList.add(to);
                        break;
                    default:
                        break;
                }
            }
            if (!firstList.isEmpty()) {
                AllSeperatePeriods.add(firstList);
            }
            if (!secondList.isEmpty()) {
                AllSeperatePeriods.add(secondList);
            }
            if (!thirdList.isEmpty()) {
                AllSeperatePeriods.add(thirdList);
            }
            if (!fourthList.isEmpty()) {
                AllSeperatePeriods.add(fourthList);
            }

            for (int i = 0; i < AllSeperatePeriods.size(); i++) {
                try {
                    ObservableList<TableObject> list = AllSeperatePeriods.get(i);
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/FrontEnds/ListItem.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    ListItemController itemController = fxmlLoader.getController();
                    itemController.setData(list);
                    grid.add(anchorPane, i + 1, 1); //(child,column,row)
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(0));
                } catch (IOException ex) {
                    Logger.getLogger(TableItemController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

//            int biggest = 0;
//            ObservableList<TableObject> l1 = FXCollections.observableArrayList();
//            ObservableList<TableObject> l2 = FXCollections.observableArrayList();
//            ObservableList<TableObject> l3 = FXCollections.observableArrayList();
//            ObservableList<TableObject> l4 = FXCollections.observableArrayList();
//            for (int i = 0; i < listOfColumns.size(); i++) {
//                ObservableList<TableObject> map = listOfColumns.get(i);
//                if (map.size() > biggest) {
//                    biggest = map.size();
//                }
//                switch (map.get(0).getPeriod()) {
//                    case "1st Period":
//                        l1 = map;
//                        break;
//                    case "2nd Period":
//                        l2 = map;
//                        break;
//                    case "3rd Period":
//                        l3 = map;
//                        break;
//                    case "4th Period":
//                        l4 = map;
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            ObservableList<TableRowItem> tableRowList = FXCollections.observableArrayList();
//            for (int i = 0; i < biggest; i++) {
//                
//                if (i < l1.size()) {
//                    TableRowItem row = new TableRowItem();
//                    TableObject TO = l1.get(i);
//                    TableIViewtem TVI = new TableIViewtem();
//                    TVI.setClassname(TO.getStuClass());
//                    TVI.setCourseCode(TO.getCourseCode());
//                    TVI.setLecturer(TO.getLecturer());
//                    TVI.setVenue(TO.getVenue());
//                    TVI.setPeriod(TO.getPeriod());
//                    row.setCol1(TVI.ToString());
//                    tableRowList.add(row);
//                }
//                if (i < l2.size()) {
//                    TableRowItem row = new TableRowItem();
//                    TableObject TO = l2.get(i);
//                    TableIViewtem TVI = new TableIViewtem();
//                    TVI.setClassname(TO.getStuClass());
//                    TVI.setCourseCode(TO.getCourseCode());
//                    TVI.setLecturer(TO.getLecturer());
//                    TVI.setVenue(TO.getVenue());
//                    TVI.setPeriod(TO.getPeriod());
//                    row.setCol2(TVI.ToString());
//                    tableRowList.add(row);
//                }
//                if (i < l3.size()) {
//                    TableRowItem row = new TableRowItem();
//                    TableObject TO = l3.get(i);
//                    TableIViewtem TVI = new TableIViewtem();
//                    TVI.setClassname(TO.getStuClass());
//                    TVI.setCourseCode(TO.getCourseCode());
//                    TVI.setLecturer(TO.getLecturer());
//                    TVI.setVenue(TO.getVenue());
//                    TVI.setPeriod(TO.getPeriod());
//                    row.setCol3(TVI.ToString());
//                    tableRowList.add(row);
//                }
//                if (i < l4.size()) {
//                    TableRowItem row = new TableRowItem();
//                    TableObject TO = l4.get(i);
//                    TableIViewtem TVI = new TableIViewtem();
//                    TVI.setClassname(TO.getStuClass());
//                    TVI.setCourseCode(TO.getCourseCode());
//                    TVI.setLecturer(TO.getLecturer());
//                    TVI.setVenue(TO.getVenue());
//                    TVI.setPeriod(TO.getPeriod());
//                    row.setCol4(TVI.ToString());
//                    tableRowList.add(row);
//                }
//
//            }
//
//          
//        }
        }
    }

}
