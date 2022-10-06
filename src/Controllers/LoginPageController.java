package Controllers;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import MongoServices.DatabaseServices;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class LoginPageController implements Initializable {

    @FXML
    private Label lb_time;
    @FXML
    private ImageView bt_minimize;
    @FXML
    private ImageView bt_close;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;

    /**
     * Initializes the controller class.
     */
    GlobalFunctions GF = new GlobalFunctions();
    DatabaseServices dbServices = new DatabaseServices();
    @FXML
    private JFXButton btn_login;
    @FXML
    private Label txt_error;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GF.getTime(lb_time);

        tf_password.setText("Fk054840");
        tf_username.setText("admin");

        bt_close.setOnMouseClicked(GF::closeWindow);
        //here we handle the minimize  button
        bt_minimize.setOnMouseClicked((Event event) -> {
            Stage thisStage = (Stage) bt_minimize.getScene().getWindow();
            thisStage.setIconified(true);
        });

        //here we hanle login
        btn_login.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                logUserIn(event);
            }
        });
    }

    private void logUserIn(Event event) {

        LoadingDailog loading = new LoadingDailog("Loging in........");
        if (!tf_username.getText().isEmpty() && !tf_password.getText().isEmpty()) {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                public Boolean call() {
                    return dbServices.loginAdmin(tf_username.getText(), tf_password.getText());
                }
            };

            task.setOnRunning((e) -> loading.show());
            task.setOnSucceeded((e) -> {
                loading.close();
                try {
                    Boolean returnValue = task.get();
                    if (returnValue) {
                        if (!"admin".equals(tf_password.getText())) {
                            txt_error.setText("");
                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();
                            Parent root;
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                            root = FXMLLoader.load(getClass().getResource("/FrontEnds/MainPage.fxml"));
                            stage.setX(bounds.getMinX());
                            stage.setY(bounds.getMinY());
                            stage.setWidth(bounds.getWidth());
                            stage.setHeight(bounds.getHeight());
                            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                            Scene scene = new Scene(root);
                            stage.setScene(scene);

                        } else {
                            Parent root = FXMLLoader.load(getClass().getResource("/FrontEnds/NewPassword.fxml"));
                            GF.openNewWindow(root);

                        }
                    } else {
                        txt_error.setText("Incorrect UserName or Password");
                    }
                } catch (InterruptedException | ExecutionException | IOException ex) {
                    Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            task.setOnFailed((e) -> {
                loading.close();
            });
            new Thread(task).start();
        } else {
            txt_error.setText("Fields Can not be Empty");
        }

    }

}
