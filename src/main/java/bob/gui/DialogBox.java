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
    private Label messageLabel;

    @FXML
    private ImageView avatarImageView;

    private DialogBox(String messageText) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box.", exception);
        }
        messageLabel.setText(messageText);
    }

    /**
     * Creates a right-aligned dialog containing a user command.
     *
     * @param messageText User command to display.
     * @return Dialog box for the user.
     */
    public static DialogBox createUserDialog(String messageText) {
        DialogBox dialogBox = new DialogBox(messageText);
        dialogBox.getStyleClass().add("user-dialog");
        dialogBox.messageLabel.getStyleClass().add("user-bubble");
        dialogBox.avatarImageView.setManaged(false);
        dialogBox.avatarImageView.setVisible(false);
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog containing Bob's response and avatar.
     *
     * @param messageText Bob's response to display.
     * @param bobAvatar Bob's avatar.
     * @return Dialog box for Bob.
     */
    public static DialogBox createBobDialog(String messageText, Image bobAvatar) {
        DialogBox dialogBox = new DialogBox(messageText);
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add("bob-dialog");
        dialogBox.messageLabel.getStyleClass().add("bob-bubble");
        dialogBox.avatarImageView.setImage(bobAvatar);
        dialogBox.avatarImageView.setClip(new Circle(22, 22, 22));
        dialogBox.getChildren().setAll(dialogBox.avatarImageView, dialogBox.messageLabel);
        return dialogBox;
    }
}
