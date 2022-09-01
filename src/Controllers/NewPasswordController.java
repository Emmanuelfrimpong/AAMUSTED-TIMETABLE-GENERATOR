package Controllers;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.conversions.Bson;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class NewPasswordController implements Initializable {

    @FXML
    private TextField tf_newPassword;
    @FXML
    private ImageView btn_close;
    @FXML
    private Text txt_error;

    /**
     * Initializes the controller class.
     */
    double[] xOffset = {0}, yOffset = {0};
    DatabaseServices MA = new DatabaseServices();
    GlobalFunctions gf = new GlobalFunctions();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_error.setText("");
        
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        if (!tf_newPassword.getText().isEmpty() && tf_newPassword.getText().length() > 6) {
            txt_error.setText("");
            Bson filter = eq("id", "admin".hashCode());
            Bson updates = set("password", tf_newPassword.getText());
            if (MA.updateDocument( "Admin", filter, updates) != null) {
                gf.inforAlert("Change of Password", "Password Changes Successfully.. Login Again", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }

        } else {
            txt_error.setText("Password can not be less than 6 Characters");
        }
    }

    @FXML
    private void handleCloseWindow(MouseEvent event) {
        gf.closeWindow(event);
    }

    @FXML
    private void handleMouseDrag(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        }
    }

    @FXML
    private void handleMousePress(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
                if (!tf_newPassword.getText().isEmpty() && tf_newPassword.getText().length() > 6) {
                    txt_error.setText("");
                    Bson filter = eq("id", "admin".hashCode());
                    Bson updates = set("password", tf_newPassword.getText());
                    if (MA.updateDocument("admin", filter, updates) != null) {
                        gf.inforAlert("Change of Password", "Password Changes Successfully.. Login Again", Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }

                } else {
                    txt_error.setText("Password can not be less than 6 Characters");
                }
            }
    }

}
