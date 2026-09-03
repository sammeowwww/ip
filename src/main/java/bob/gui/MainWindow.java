package bob.gui;

import java.util.Objects;

import bob.Bob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Bob's main chat window.
 */
public class MainWindow extends AnchorPane {
    private final Image bobAvatar = new Image(Objects.requireNonNull(
            getClass().getResourceAsStream("/images/bob.png")));

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Bob bob;

    /**
     * Configures behavior that is available after FXML injection.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the chatbot used to answer commands.
     *
     * @param bob Chatbot backing this window.
     */
    public void setBob(Bob bob) {
        this.bob = bob;
        dialogContainer.getChildren().add(
                DialogBox.createBobDialog(bob.getStartupMessage(), bobAvatar));
        userInput.requestFocus();
    }

    /**
     * Sends the entered command to Bob and displays both sides of the conversation.
     */
    @FXML
    public void submitUserCommand() {
        String userCommand = userInput.getText().trim();
        if (userCommand.isEmpty() || bob == null) {
            return;
        }

        String bobResponse = bob.executeUserCommand(userCommand);
        dialogContainer.getChildren().addAll(
                DialogBox.createUserDialog(userCommand),
                DialogBox.createBobDialog(bobResponse, bobAvatar));
        userInput.clear();
    }
}
