package GlobalFunctions;

import Controllers.ToastController;
import Objects.ExcellHeaders;
import aamusted.timetable.generator.AAMUSTEDTIMETABLEGENERATOR;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
        alert.setContentText(message);
        alert.show();
    }

    public void closeWindow(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Close the Application?",
                ButtonType.YES, ButtonType.NO);
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

    public void getTime(Label label) {
        DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss a");
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler() {
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
            Logger.getLogger(AAMUSTEDTIMETABLEGENERATOR.class.getName()).log(Level.SEVERE, null, ex);
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
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public boolean verifyStudentExcel(Row titleRow) {

        String titleOne = titleRow.getCell(0).getStringCellValue().toLowerCase();
        String titleTwo = titleRow.getCell(1).getStringCellValue().toLowerCase();
        String titleThree = titleRow.getCell(2).getStringCellValue().toLowerCase();
        String titleFour = titleRow.getCell(3).getStringCellValue().toLowerCase();
        String titleFive = titleRow.getCell(4).getStringCellValue().toLowerCase();

        return titleOne.equals(ExcellHeaders.getLevel()) && titleTwo.equals(ExcellHeaders.getClassName()) && titleThree.equals(ExcellHeaders.getClassSize())
                && titleFour.equals(ExcellHeaders.getHasDisability()) && titleFive.equals(ExcellHeaders.getCourses());

    }

    public void showToast(String message, Stage parentState) {
        final double midX = (parentState.getX() + parentState.getWidth()) / 2;
        final double midY = (parentState.getY() + parentState.getHeight()) / 2;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontEnds/toast.fxml"));
            Parent parent = fxmlLoader.load();
            ToastController controller = fxmlLoader.<ToastController>getController();
            controller.setMessage(message);
            Stage dialog = new Stage();
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setResizable(false);
            dialog.widthProperty().addListener((observable, oldValue, newValue) -> {
                dialog.setX(midX - newValue.intValue() / 2);
            });

            dialog.heightProperty().addListener((observable, oldValue, newValue) -> {
                dialog.setY(midY - newValue.intValue() / 2);

            });
            dialog.show();
            new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> {
                        dialog.close();
                    })).play();
        } catch (IOException ex) {
            inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public boolean verifyExcelFile(Row classTitleRow, Row courseTitleRow) {
        String classOne = classTitleRow.getCell(0).getStringCellValue().toLowerCase();
        String classTwo = classTitleRow.getCell(1).getStringCellValue().toLowerCase();
        String classThree = classTitleRow.getCell(2).getStringCellValue().toLowerCase();
        String classFour = classTitleRow.getCell(3).getStringCellValue().toLowerCase();
        String classFive = classTitleRow.getCell(4).getStringCellValue().toLowerCase();
        String courseOne = courseTitleRow.getCell(0).getStringCellValue().toLowerCase();
        String courseTwo = courseTitleRow.getCell(1).getStringCellValue().toLowerCase();
        String courseThree = courseTitleRow.getCell(2).getStringCellValue().toLowerCase();
        String courseFour = courseTitleRow.getCell(3).getStringCellValue().toLowerCase();
        String courseFive = courseTitleRow.getCell(4).getStringCellValue().toLowerCase();
        String courseSix = courseTitleRow.getCell(5).getStringCellValue().toLowerCase();
        String courseSeven = courseTitleRow.getCell(6).getStringCellValue().toLowerCase();
        return classOne.equals(ExcellHeaders.getLevel()) && classTwo.equals(ExcellHeaders.getClassName()) && classThree.equals(ExcellHeaders.getClassSize())
                && classFour.equals(ExcellHeaders.getHasDisability()) && classFive.equals(ExcellHeaders.getCourses())
                && courseOne.equals(ExcellHeaders.getCourseCode()) && courseTwo.equals(ExcellHeaders.getCouseTitle()) && courseThree.equals(ExcellHeaders.getCreaditHourse())
                && courseFour.equals(ExcellHeaders.getSpecialVenue()) && courseFive.equals(ExcellHeaders.getLecturerName()) && courseSix.equals(ExcellHeaders.getLecturerEmail())
                && courseSeven.equals(ExcellHeaders.getLecturerPhone());

    }

    public Alert showAlert(String message, ButtonType submit, ButtonType cancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/Styles/dialog.css");

        return alert;
    }

    public Stage LoadingDailog(String message) {
        Stage loadingStage = new Stage();
        RingProgressIndicator indicator = new RingProgressIndicator();
        indicator.makeIndeterminate();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        VBox cont = new VBox();
        cont.setAlignment(Pos.CENTER);
        Label lb = new Label(message);
        cont.getChildren().addAll(indicator, lb);
        Scene scene = new Scene(cont);
        loadingStage.setX(bounds.getMinX());
        loadingStage.setY(bounds.getMinY());
        loadingStage.setWidth(bounds.getWidth());
        loadingStage.setHeight(bounds.getHeight());
        loadingStage.setOpacity(0.95);
        loadingStage.setResizable(false);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        loadingStage.setScene(scene);

        return loadingStage;

    }

    public boolean verifyAfricanSheet(Row courseTitleRow) {
        String courseOne = courseTitleRow.getCell(0).getStringCellValue().toLowerCase();
        String courseTwo = courseTitleRow.getCell(1).getStringCellValue().toLowerCase();
        String courseThree = courseTitleRow.getCell(2).getStringCellValue().toLowerCase();
        String courseFour = courseTitleRow.getCell(3).getStringCellValue().toLowerCase();
        String courseFive = courseTitleRow.getCell(4).getStringCellValue().toLowerCase();
        String courseSix = courseTitleRow.getCell(5).getStringCellValue().toLowerCase();
        String courseSeven = courseTitleRow.getCell(6).getStringCellValue().toLowerCase();

        return courseOne.equals(ExcellHeaders.getCourseCode()) && courseTwo.equals(ExcellHeaders.getCouseTitle()) && courseThree.equals(ExcellHeaders.getCreaditHourse())
                && courseFour.equals(ExcellHeaders.getSpecialVenue()) && courseFive.equals(ExcellHeaders.getLecturerName()) && courseSix.equals(ExcellHeaders.getLecturerEmail())
                && courseSeven.equals(ExcellHeaders.getLecturerPhone());

    }
}
