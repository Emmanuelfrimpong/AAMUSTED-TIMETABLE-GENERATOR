package Controllers;

import Objects.TableObject;
import Objects.TableRowItem;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class TableItemController implements Initializable {

    @FXML
    private Label lb_day;
    @FXML
    private TableView<TableRowItem> tv_table;

    /**
     * Initializes the controller class.
     */
    Callback factory;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final List<String> colors = Arrays.asList(
        "blue",
        "green",
        "red",
        "violet",
        "yellow"
);

 factory = new Callback<TableColumn<TableRowItem, Object>, TableCell<TableRowItem, Object>>() {

    private int columns = tv_table.getColumns().size();

    @Override
    public TableCell<TableRowItem, Object> call(TableColumn<TableRowItem, Object> param) {
        return new TableCell<TableRowItem, Object>() {
            private int columnIndex = param.getTableView().getColumns().indexOf(param);
            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                // select color based on index of row/column
                if (i >= 0) {
                    // select color repeating the color, if we run out of colors
                    String color = colors.get((i * columns + columnIndex) % colors.size());
                    this.setStyle("-fx-my-cell-background: " + color + ";");
                    System.out.println(getStyle());
                }
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                // assign item's toString value as text
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }

        };
    }

};

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

            ObservableList<TableRowItem> rows = FXCollections.observableArrayList();
            ObservableList<TableObject> list1 = FXCollections.observableArrayList();
            ObservableList<TableObject> list2 = FXCollections.observableArrayList();
            ObservableList<TableObject> list3 = FXCollections.observableArrayList();
            ObservableList<TableObject> list4 = FXCollections.observableArrayList();
            for (ObservableList<TableObject> list : AllSeperatePeriods) {
                TableColumn<TableRowItem, String> col = new TableColumn(list.get(0).getPeriod());
                switch (list.get(0).getPeriod()) {
                    case "1st Period":
                        col.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCol1()));
                        col.setCellFactory(factory);
                        list1 = list;
                        break;
                    case "2nd Period":
                        col.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCol2()));
                        list2 = list;
                        break;
                    case "3rd Period":
                        col.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCol3()));
                        list3 = list;
                        break;
                    case "4th Period":
                        list4 = list;
                        col.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getCol4()));
                        break;
                    default:
                        break;

                }
                tv_table.getColumns().add(col);

            }
            int biggest = 0;
            if (!list1.isEmpty()) {
                if (list1.size() > biggest) {
                    biggest = list1.size();
                }
            }
            if (!list2.isEmpty()) {
                if (list2.size() > biggest) {
                    biggest = list2.size();
                }
            }
            if (!list3.isEmpty()) {
                if (list3.size() > biggest) {
                    biggest = list3.size();
                }
            }
            if (!list4.isEmpty()) {
                if (list4.size() > biggest) {
                    biggest = list4.size();
                }
            }
            if (!list1.isEmpty()) {
                FXCollections.shuffle(list1);
                for (int i = 0; i < biggest; i++) {
                    if (i < list1.size()) {
                        if (i < rows.size()) {
                            rows.get(i).setCol1(list1.get(i).ToString());
                        } else {
                            TableRowItem ro = new TableRowItem();
                            ro.setCol1(list1.get(i).ToString());
                            rows.add(ro);
                        }
                    }
                }
            }
                if (!list2.isEmpty()) {
                FXCollections.shuffle(list2);
                for (int i = 0; i < biggest; i++) {
                    if (i < list2.size()) {
                        if (i < rows.size()) {
                            rows.get(i).setCol2(list2.get(i).ToString());
                        } else {
                            TableRowItem ro = new TableRowItem();
                            ro.setCol2(list2.get(i).ToString());
                            rows.add(ro);
                        }
                    }
                }
            }
                if (!list3.isEmpty()) {
                FXCollections.shuffle(list3);
                for (int i = 0; i < biggest; i++) {
                    if (i < list3.size()) {
                        if (i < rows.size()) {
                            rows.get(i).setCol3(list3.get(i).ToString());
                        } else {
                            TableRowItem ro = new TableRowItem();
                            ro.setCol3(list3.get(i).ToString());
                            rows.add(ro);
                        }
                    }
                }
            }
                if (!list4.isEmpty()) {
                FXCollections.shuffle(list4);
                for (int i = 0; i < biggest; i++) {
                    if (i < list4.size()) {
                        if (i < rows.size()) {
                            rows.get(i).setCol4(list4.get(i).ToString());
                        } else {
                            TableRowItem ro = new TableRowItem();
                            ro.setCol4(list4.get(i).ToString());
                            rows.add(ro);
                        }
                    }
                }
            }

            tv_table.setItems(rows);
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
//        }
        //}
    }
}
