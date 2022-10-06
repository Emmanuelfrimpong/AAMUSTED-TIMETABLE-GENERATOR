
package Controllers;

import Objects.TableObject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class TableItemController implements Initializable {

    @FXML
    private Label lb_day;
    @FXML
    private TableView<?> tv_table;
    @FXML
    private TableColumn<?, ?> col_1;
    @FXML
    private TableColumn<?, ?> col_2;
    @FXML
    private TableColumn<?, ?> col_3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    
    public void setData(ObservableList<TableObject>tables){
        
    }
    
}
