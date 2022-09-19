package GlobalFunctions;

import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author emman
 * @param <S>
 */
public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ActionButtonTableCell(String label,String style, Function< S, S> function) {
        this.getStyleClass().add("action-button-table-cell");

        ImageView image = new ImageView();
        Image imProfile = new Image(getClass().getResourceAsStream(label));
        image.setFitHeight(18);
        image.setFitWidth(18);
        image.setImage(imProfile);
        this.actionButton = new Button();
        this.actionButton.getStylesheets().add(style);
        this.actionButton.setMinHeight(18);
        this.actionButton.setGraphic(image);
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label,String style, Function< S, S> function) {
        return param -> new ActionButtonTableCell<>(label, style,function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}
