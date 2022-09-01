
package GlobalFunctions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author emman
 */
public class GlobalFunctions {
    
        private static double xOffset = 0;
    private static double yOffset = 0;

    public String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String arr1 : arr) {
            sb.append(Character.toUpperCase(arr1.charAt(0))).append(arr1.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
        public void inforAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        alert.getDialogPane().setMinSize(400, 120);
        alert.setContentText(message);
        alert.show();
    }

    public void closeWindow(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Close the Application?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Exit");
        alert.getDialogPane().getStylesheets().add("/Styles/dialogStyle.css");
        alert.getDialogPane().setMinSize(400, 200);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        } else {
            alert.close();
        }
    }
    public void getTime( Label label) {
        DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss a");
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),new EventHandler() {
            @Override
            public void handle(Event event) {
                final Calendar cal = Calendar.getInstance();
                label.setText(format.format(cal.getTime()));
            }
        }));
           
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
     public void sideBarClick(StackPane container, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            container.getChildren().clear();
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            container.getChildren().add(root);
        } catch (IOException ex) {
            inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
     
         public void openNewWindow(Parent parent) {
        Scene scene;
        scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        parent.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        parent.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        //stage.getIcons().add(new Image("/images/image-removebg-preview.png"));
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public boolean verifyStudentExcel(Row titleRow) {
        boolean itIsValid=false;
        for (Cell cell:titleRow ){
        }
        return itIsValid;
    
    }
}
