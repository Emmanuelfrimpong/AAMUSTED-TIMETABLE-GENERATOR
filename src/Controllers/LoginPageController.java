package Controllers;

import GlobalFunctions.GlobalFunctions;
import MongoServices.DatabaseServices;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
//                Stage oldStage = (Stage) btn_login.getScene().getWindow();
//                oldStage.hide();
//                try {
//
//                    Screen screen = Screen.getPrimary();
//                    Parent root = FXMLLoader.load(getClass().getResource("/FrontEnds/MainPage.fxml"));
//                    Scene scene = new Scene(root);
//                    Stage stage = new Stage();
//                    stage.initStyle(StageStyle.UNDECORATED);
//                    stage.initStyle(StageStyle.TRANSPARENT);
//                    scene.setFill(Color.TRANSPARENT);
//                    Rectangle2D bounds = screen.getVisualBounds();
//                    stage.setX(bounds.getMinX());
//                    stage.setY(bounds.getMinY());
//                    stage.setWidth(bounds.getWidth());
//                    stage.setHeight(bounds.getHeight());
//                    stage.setResizable(false);
//                    stage.setScene(scene);
//                    //stage.getIcons().add(new Image("/images/image-removebg-preview.png"));
//                    stage.show();
//                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
//                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
//
//                } catch (IOException ex) {
//                    Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        });
    }

    private void logUserIn(Event event) {
        try {
            if (!tf_username.getText().isEmpty() && !tf_password.getText().isEmpty()) {

                if (dbServices.loginAdmin(tf_username.getText(), tf_password.getText())) {
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
            } else {
                txt_error.setText("Fields Can not be Empty");
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
