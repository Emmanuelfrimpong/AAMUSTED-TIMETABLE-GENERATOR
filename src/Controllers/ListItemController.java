package Controllers;

import Objects.TableIViewtem;
import Objects.TableObject;
import Objects.TableRowItem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class ListItemController implements Initializable {

    @FXML
    private Label lb;
    @FXML
    private ListView<String> view;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(ObservableList<TableObject> data) {
        lb.setText(data.get(0).getPeriod());
        for (TableObject TO : data) {
            TableIViewtem TVI = new TableIViewtem();
            TVI.setClassname(TO.getStuClass());
            TVI.setCourseCode(TO.getCourseCode());
            TVI.setLecturer(TO.getLecturer());
            TVI.setVenue(TO.getVenue());
            TVI.setPeriod(TO.getPeriod());
            view.getItems().add(TVI.ToString());
        }
    }

}
