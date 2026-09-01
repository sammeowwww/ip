package bob.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Displays one user or Bob message in the conversation.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box.", exception);
        }
        dialog.setText(text);
    }

    /**
     * Creates a right-aligned dialog containing a user command.
     *
     * @param text User command to display.
     * @return Dialog box for the user.
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text);
        dialogBox.getStyleClass().add("user-dialog");
        dialogBox.dialog.getStyleClass().add("user-bubble");
        dialogBox.displayPicture.setManaged(false);
        dialogBox.displayPicture.setVisible(false);
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog containing Bob's response and avatar.
     *
     * @param text Bob's response to display.
     * @param image Bob's avatar.
     * @return Dialog box for Bob.
     */
    public static DialogBox getBobDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text);
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add("bob-dialog");
        dialogBox.dialog.getStyleClass().add("bob-bubble");
        dialogBox.displayPicture.setImage(image);
        dialogBox.displayPicture.setClip(new Circle(22, 22, 22));
        dialogBox.getChildren().setAll(dialogBox.displayPicture, dialogBox.dialog);
        return dialogBox;
    }
}
