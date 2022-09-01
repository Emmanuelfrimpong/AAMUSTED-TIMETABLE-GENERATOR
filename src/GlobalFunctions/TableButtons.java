/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GlobalFunctions;

import Objects.StudentsObject;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author emman
 */
public class TableButtons {

    public Callback<TableColumn<StudentsObject, Button>, TableCell<StudentsObject, Button>> addEditButton() {

        Callback<TableColumn<StudentsObject, Button>, TableCell<StudentsObject, Button>> cellFactory = (final TableColumn<StudentsObject, Button> param) -> {
            final TableCell<StudentsObject, Button> cell = new TableCell<StudentsObject, Button>() {

                private final Button btn = new Button("Action");
                ImageView imageView = new ImageView(getClass().getResource("/images/edit.png").toExternalForm());

                {
                    btn.setGraphic(imageView);
                    btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    imageView.fitWidthProperty().bind(btn.widthProperty().divide(10));
                    imageView.setPreserveRatio(true);
                    //Important otherwise button will wrap to text + graphic size (no resizing on scaling).
                    btn.setMaxWidth(Double.MAX_VALUE);
                    btn.setOnAction((ActionEvent event) -> {
                        StudentsObject data = getTableView().getItems().get(getIndex());

                    });
                }

                @Override
                public void updateItem(Button item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };

        return cellFactory;
    }
    
    public Callback<TableColumn<StudentsObject, Button>, TableCell<StudentsObject, Button>> addDeleteButton() {

        Callback<TableColumn<StudentsObject, Button>, TableCell<StudentsObject, Button>> cellFactory = (final TableColumn<StudentsObject, Button> param) -> {
            final TableCell<StudentsObject, Button> cell = new TableCell<StudentsObject, Button>() {

                private final Button btn = new Button("Action");
                ImageView imageView = new ImageView(getClass().getResource("/images/edit.png").toExternalForm());

                {
                    btn.setGraphic(imageView);
                    btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    imageView.fitWidthProperty().bind(btn.widthProperty().divide(10));
                    imageView.setPreserveRatio(true);
                    //Important otherwise button will wrap to text + graphic size (no resizing on scaling).
                    btn.setMaxWidth(Double.MAX_VALUE);
                    btn.setOnAction((ActionEvent event) -> {
                        StudentsObject data = getTableView().getItems().get(getIndex());

                    });
                }

                @Override
                public void updateItem(Button item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };

        return cellFactory;
    }
}
